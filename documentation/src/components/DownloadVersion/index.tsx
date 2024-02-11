import React from "react";
import {
  useDocsVersion,
  useDocsPreferredVersion,
} from "@docusaurus/theme-common/internal";
import Link from "@docusaurus/Link";

export default function DownloadVersion(): JSX.Element {
  const versionLabel = useDocsVersion().label;

  if (versionLabel === "Beta⚠️") {
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
    useDocsPreferredVersion().preferredVersion.label === versionLabel
  ) {
    return (
      <span>
        The <b>Latest</b> version via
        <Link
          to={`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(
            versionLabel
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
        The <b>Outdated</b> version {versionLabel} via
        <Link
          to={`https://modrinth.com/plugin/typewriter/version/${encodeURIComponent(
            versionLabel
          )}`}
        >
          {" "}
          this Modrinth link
        </Link>
      </span>
    );
  }
}
