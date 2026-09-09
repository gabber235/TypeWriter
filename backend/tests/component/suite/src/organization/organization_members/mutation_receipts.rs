use super::*;

#[component_test(OrganizationMembers)]
async fn role_batch_receipt_preserves_complete_result_without_reapplying(
    context: &mut TestContext<OrganizationMembers>,
) -> TestResult {
    let database = context
        .extension::<DatabaseHandle>()
        .ok_or_else(|| anyhow::anyhow!("database handle missing"))?;
    seed_organization(&database).await?;
    let writer = role_key(&database, "writer").await?;
    let request = UpdateOrganizationMemberRolesRequest {
        operation_id: crate::framework::operation_id(),
        user_ids: vec![
            skir_record_id("user", "founder"),
            skir_record_id("user", "member"),
        ],
        role_ids: vec![skir_record_id("organization_role", &writer)],
        _unrecognized: None,
    };
    for expected_name in ["member", "renamed_after_commit"] {
        context.messaging_mock()?.expect_publish("typewriter.to.organization.alpha.members.watch")
            .body_matches(move |body| {
                matches!(WatchOrganizationMembersResponse::serializer().from_bytes(body, UnrecognizedValues::Drop),
                    Ok(WatchOrganizationMembersResponse::List(members)) if members.iter().any(|member|
                        member.user_id.key.to_string() == "member" && member.name.as_deref() == Some(expected_name)))
            });
    }
    let original = update(context, &request).await?;
    assert!(
        matches!(&original, UpdateOrganizationMemberRolesResponse::Success(members) if members.len() == 2)
    );
    database
        .execute("UPDATE user:member SET name = 'renamed_after_commit'")
        .await?;
    let replay = update(context, &request).await?;
    assert_eq!(original, replay);
    assert_jm!(database.query_json("RETURN { receipts: count(SELECT id FROM mutation_receipt), name: user:member.name }").await?, { "receipts": 1, "name": "renamed_after_commit" });

    let altered = UpdateOrganizationMemberRolesRequest {
        user_ids: vec![skir_record_id("user", "member")],
        ..request
    };
    assert!(matches!(
        update(context, &altered).await?,
        UpdateOrganizationMemberRolesResponse::OperationIdentityReusedError(_)
    ));
    Ok(())
}

#[component_test(OrganizationMembers)]
async fn invalid_middle_member_rejects_entire_batch_without_receipt(
    context: &mut TestContext<OrganizationMembers>,
) -> TestResult {
    let database = context
        .extension::<DatabaseHandle>()
        .ok_or_else(|| anyhow::anyhow!("database handle missing"))?;
    seed_organization(&database).await?;
    let writer = role_key(&database, "writer").await?;
    let response = update(
        context,
        &UpdateOrganizationMemberRolesRequest {
            operation_id: crate::framework::operation_id(),
            user_ids: vec![
                skir_record_id("user", "founder"),
                skir_record_id("user", "missing"),
                skir_record_id("user", "member"),
            ],
            role_ids: vec![skir_record_id("organization_role", &writer)],
            _unrecognized: None,
        },
    )
    .await?;
    assert!(matches!(
        response,
        UpdateOrganizationMemberRolesResponse::UserNotFoundError(_)
    ));
    assert_jm!(database.query_json("RETURN { receipts: count(SELECT id FROM mutation_receipt), founder_roles: (SELECT VALUE roles.name FROM ONLY member_of WHERE in = user:founder AND out = organization:alpha FETCH roles) }").await?, { "receipts": 0, "founder_roles": ["founder"] });
    Ok(())
}

async fn update(
    context: &mut TestContext<OrganizationMembers>,
    request: &UpdateOrganizationMemberRolesRequest,
) -> anyhow::Result<UpdateOrganizationMemberRolesResponse> {
    Ok(context
        .messaging()?
        .request_skir(
            "typewriter.from.user.founder.organization.alpha.members.update",
            request,
            UpdateOrganizationMemberRolesRequest::serializer(),
            UpdateOrganizationMemberRolesResponse::serializer(),
            Duration::from_secs(2),
            UnrecognizedValues::Drop,
        )
        .await?)
}
