part of "services.dart";

extension _HostConfigurationValue on HostEditorSnapshot {
  RecordValue _configurationValue(
    TopologyRealm? realm,
    TopologyEngine? engine,
  ) => RecordValue({
    "realm": realm == null
        ? _draftVariant(_realmDisabled)
        : _draftVariant(_realmHosted, {
            "target": StringValue(
              _encodeTarget(
                realm.targetEngine.engineId,
                realm.targetEngine.versionConstraint,
              ),
            ),
          }),
    "engine": engine == null
        ? _draftVariant(_engineDisabled)
        : _draftVariant(_engineEnabled, {
            "target": StringValue(
              _encodeTarget(
                engine.target.engineId,
                engine.target.versionConstraint,
              ),
            ),
            "realm": StringValue(
              realm?.realmId == engine.realm.realmId &&
                      realm?.targetEngine == engine.target
                  ? ""
                  : engine.realm.realmId.id,
            ),
          }),
  });

  List<TypeDiagnostic> _configurationIssues(DataValue value) {
    final issues = <TypeDiagnostic>[];
    void issue(String path, String message) => issues.add(
      TypeDiagnostic(
        code: TypeDiagnosticCode.invalidValue,
        message: message,
        path: DataPath(path.split(".").map(FieldPathSegment.new).toList()),
      ),
    );
    final realm = DataPath.root.field("realm").read(value).valueOrNull;
    final engine = DataPath.root.field("engine").read(value).valueOrNull;
    if (realm is! PolymorphicValue || engine is! PolymorphicValue) {
      issue("realm", "Choose a workload configuration");
      return issues;
    }
    if (realm.concreteType == _realmHosted) {
      if (!host.canHostRealm) issue("realm", "This host cannot run a Realm");
      if (_decodeTarget(_draftString(realm, "target"), _realmTargets) == null) {
        issue("realm.target", "Choose a supported Realm target");
      }
    }
    if (engine.concreteType != _engineEnabled) return issues;
    if (_decodeTarget(_draftString(engine, "target"), _engineTargets) == null) {
      issue("engine.target", "Choose a supported engine target");
    }
    if (!_usesHostedRealm(realm, engine) &&
        !topology.realmInstances.any(
          (r) =>
              r.realmId.id == _draftString(engine, "realm") &&
              r.ownerHost.id != host.hostId &&
              _encodeTarget(
                    r.targetEngine.engineId,
                    r.targetEngine.versionConstraint,
                  ) ==
                  _draftString(engine, "target"),
        )) {
      issue("engine.realm", "Choose an available Realm");
    }
    return issues;
  }

  skir.HostExecutionConfiguration? _decodeExecution(DataValue value) {
    if (_configurationIssues(value).isNotEmpty) return null;
    final realm =
        DataPath.root.field("realm").read(value).valueOrNull!
            as PolymorphicValue;
    final engine =
        DataPath.root.field("engine").read(value).valueOrNull!
            as PolymorphicValue;
    return skir.HostExecutionConfiguration(
      realm: realm.concreteType == _realmHosted
          ? skir.HostedRealmConfiguration(
              primaryEngine: _decodeTarget(
                _draftString(realm, "target"),
                _realmTargets,
              )!.toSkir(),
            )
          : null,
      primaryEngine: engine.concreteType == _engineEnabled
          ? skir.HostedEngineConfiguration(
              target: _decodeTarget(
                _draftString(engine, "target"),
                _engineTargets,
              )!.toSkir(),
              realm: _usesHostedRealm(realm, engine)
                  ? skir.EngineRealmSelection.hostedRealm
                  : skir.EngineRealmSelection.createExistingRealm(
                      realmId: topology.realmInstances
                          .singleWhere(
                            (r) =>
                                r.realmId.id == _draftString(engine, "realm") &&
                                r.ownerHost.id != host.hostId,
                          )
                          .realmId,
                    ),
            )
          : null,
    );
  }
}

String? _draftString(DataValue value, String field) =>
    DataPath.root.field(field).read(value).valueOrNull?.stringOrNull;

bool _usesHostedRealm(PolymorphicValue realm, PolymorphicValue engine) =>
    realm.concreteType == _realmHosted &&
    engine.concreteType == _engineEnabled &&
    (_draftString(realm, "target")?.isNotEmpty ?? false) &&
    _draftString(realm, "target") == _draftString(engine, "target");
