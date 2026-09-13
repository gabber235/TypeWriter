//! Public facade for embedded wasmCloud component tests.
//!
//! Test crates use the re exported model, fixture builder, mocks, and attribute macros from this
//! crate. Registration is inventory based, so the xtask can validate and select cases without
//! executing them first.

#![forbid(unsafe_code)]

pub use component_test_core::*;
pub use component_test_macros::{component_fixture, component_test};
pub use component_test_model::*;
pub use inventory;

/// Returns all statically registered fixture descriptors in deterministic order.
pub fn registered_fixtures() -> Result<Vec<&'static FixtureDescriptor>, ModelError> {
    let mut fixtures = inventory::iter::<FixtureRegistration>
        .into_iter()
        .map(|registration| registration.descriptor)
        .collect::<Vec<_>>();
    fixtures.sort_by_key(|fixture| fixture.id);
    validate_catalog(fixtures.iter().copied(), registered_tests_unchecked())?;
    Ok(fixtures)
}

/// Returns all statically registered test descriptors in deterministic order.
pub fn registered_tests() -> Result<Vec<&'static TestDescriptor>, ModelError> {
    let fixtures = inventory::iter::<FixtureRegistration>
        .into_iter()
        .map(|registration| registration.descriptor)
        .collect::<Vec<_>>();
    let mut tests = registered_tests_unchecked().collect::<Vec<_>>();
    tests.sort_by_key(|test| test.exact_name);
    validate_catalog(fixtures, tests.iter().copied())?;
    Ok(tests)
}

fn registered_tests_unchecked() -> impl Iterator<Item = &'static TestDescriptor> {
    inventory::iter::<TestRegistration>
        .into_iter()
        .map(|registration| registration.descriptor)
}
