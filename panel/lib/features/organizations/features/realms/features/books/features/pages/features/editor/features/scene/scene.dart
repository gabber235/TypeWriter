/// Timeline scene editing for pages whose catalog kind is timeline based.
///
/// The scene reads the page projection, where canonical authoring values may
/// already include local drafts. Entries become timeline tracks and their
/// outward links determine the cue hierarchy. Cue selection uses the shared
/// inspector, while committed frame changes return to the page element owner.
library;

export "application/application.dart";
export "presentation/presentation.dart";
