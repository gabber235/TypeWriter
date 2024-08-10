import React from "react";
import clsx from "clsx";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import Link from "@docusaurus/Link";
import Translate from "@docusaurus/Translate";
import {
    useActivePlugin,
    useDocVersionSuggestions,
} from "@docusaurus/plugin-content-docs/client";
import { ThemeClassNames } from "@docusaurus/theme-common";
import Admonition from '@theme/Admonition';
import {
    useDocsPreferredVersion,
    useDocsVersion,
} from "@docusaurus/plugin-content-docs/client";

function UnreleasedVersionLabel({ siteTitle, versionMetadata }) {
    return (
        <Translate
            id="theme.docs.versions.unreleasedVersionLabel"
            description="The label used to tell the user that they're browsing an unreleased doc version"
            values={{
                siteTitle,
                versionLabel: <b>{versionMetadata.label}</b>,
                betatext: <b>beta</b>,
            }}
        >
            {
                "You are currently looking at the {betatext} documentation of Typewriter. This means that the documentation may be incomplete or outdated."
            }
        </Translate>
    );
}
function UnmaintainedVersionLabel({ siteTitle, versionMetadata }) {
    return (
        <Translate
            id="theme.docs.versions.unmaintainedVersionLabel"
            description="The label used to tell the user that they're browsing an unmaintained doc version"
            values={{
                siteTitle,
                versionLabel: <b>{versionMetadata.label}</b>,
            }}
        >
            {
                "This is a version of Typewriter {versionLabel} is no longer actively maintained."
            }
        </Translate>
    );
}
const BannerLabelComponents = {
    unreleased: UnreleasedVersionLabel,
    unmaintained: UnmaintainedVersionLabel,
};
function BannerLabel(props) {
    const BannerLabelComponent =
        BannerLabelComponents[props.versionMetadata.banner];
    return <BannerLabelComponent {...props} />;
}
function LatestVersionSuggestionLabel({ versionLabel, to, onClick }) {
    return (
        <Translate
            id="theme.docs.versions.latestVersionSuggestionLabel"
            description="The label used to tell the user to check the latest version"
            values={{
                versionLabel,
                latestVersionLink: (
                    <b>
                        <Link to={to} onClick={onClick}>
                            <Translate
                                id="theme.docs.versions.latestVersionLinkLabel"
                                description="The label used for the latest version suggestion link label"
                            >
                                latest version
                            </Translate>
                        </Link>
                    </b>
                ),
            }}
        >
            {
                "For the latest released version, see the {latestVersionLink} ({versionLabel})."
            }
        </Translate>
    );
}
function DocVersionBannerEnabled({ className, versionMetadata }) {
    const {
        siteConfig: { title: siteTitle },
    } = useDocusaurusContext();

    const { pluginId } = useActivePlugin({ failfast: true });
    const getVersionMainDoc = (version) =>
        version.docs.find((doc) => doc.id === version.mainDocId);

    const { savePreferredVersionName } = useDocsPreferredVersion(pluginId);
    const { latestDocSuggestion, latestVersionSuggestion } =
        useDocVersionSuggestions(pluginId);
    // Try to link to same doc in latest version (not always possible), falling
    // back to main doc of latest version
    const latestVersionSuggestedDoc =
        latestDocSuggestion ?? getVersionMainDoc(latestVersionSuggestion);

    const title = versionMetadata.banner === "unreleased" ? "Warning: Beta Version" : "Warning: Unmaintained Version";

    return (
        <Admonition type="danger" title={title}>
            <div
                className={clsx(
                    className,
                    ThemeClassNames.docs.docVersionBanner,
                )}
                role="alert"
            >
                <div>
                    <BannerLabel siteTitle={siteTitle} versionMetadata={versionMetadata} />
                </div>
                <div className="margin-top--md">
                    <LatestVersionSuggestionLabel
                        versionLabel={latestVersionSuggestion.label}
                        to={latestVersionSuggestedDoc.path}
                        onClick={() => savePreferredVersionName(latestVersionSuggestion.name)}
                    />
                </div>
            </div>
        </Admonition>
    );
}
export default function DocVersionBanner({ className }) {
    const versionMetadata = useDocsVersion();
    if (versionMetadata.banner) {
        return (
            <DocVersionBannerEnabled
                className={className}
                versionMetadata={versionMetadata}
            />
        );
    }
    return null;
}
