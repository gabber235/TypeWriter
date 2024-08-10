import React from "react";
import {
    useActiveVersion,
    useLatestVersion,
} from "@docusaurus/plugin-content-docs/client";
import Link from "@docusaurus/Link";

export default function DownloadVersion(): JSX.Element {
    const activeVersion = useActiveVersion(undefined)?.name;
    const latestVersion = useLatestVersion(undefined)?.name;

    if (activeVersion === "current") {
        return (
            <span>
                the <b>Beta</b> version via
                <Link to={`https://modrinth.com/plugin/typewriter/versions?c=beta`}>
                    {" "}
                    this Modrinth link
                </Link>
            </span>
        );
    } else if (
        latestVersion === activeVersion
    ) {
        return (
            <span>
                The <b>Latest</b> version via
                <Link
                    to={`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(
                        activeVersion
                    )}`}
                >
                    {" "}
                    this Modrinth link
                </Link>
            </span>
        );
    } else {
        return (
            <span>
                The <b>Outdated</b> version {activeVersion} via
                <Link
                    to={`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(
                        activeVersion
                    )}`}
                >
                    {" "}
                    this Modrinth link
                </Link>
            </span>
        );
    }
}
