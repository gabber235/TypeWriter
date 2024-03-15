import Link from "@docusaurus/Link";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import HomepageFeatures from "@site/src/components/HomepageFeatures";
import Layout from "@theme/Layout";
import clsx from "clsx";
import React from "react";

import styles from "./index.module.css";

function HomepageHeader() {
    const { siteConfig } = useDocusaurusContext();

    return (
        <header className={clsx("hero hero--primary", styles.heroBanner)}>
            {/* Video Background */}
            <div id="video-container" className={styles.videoContainer}>
                <video autoPlay loop muted className={styles.videoBackground}>
                    <source
                        src={require("@site/static/videos/backgroundvideo-homepage.webm").default}
                        type="video/webm"
                    />
                    Your browser does not support the video tag.
                </video>

                {/* Add overlay element */}
                <div className={styles.overlay}></div>
            </div>

            {/* Your existing content */}
            <div className={clsx("container", styles.contentContainer)}>
                <h1 className="hero__title">{siteConfig.title}</h1>
                <p className="hero__subtitle">{siteConfig.tagline}</p>
                <div className={styles.buttons}>
                    <Link
                        className={clsx(
                            "button button--secondary button--lg",
                            styles.startButton
                        )}
                        to="/docs/home"
                    >
                        Get Started →
                    </Link>
                </div>
            </div>
        </header>
    );
}

export default function Home(): JSX.Element {
    const { siteConfig } = useDocusaurusContext();
    return (
        <Layout title={`${siteConfig.title}`} description={`${siteConfig.tagline}`}>
            <HomepageHeader />
            <main>
                <HomepageFeatures />
            </main>
        </Layout>
    );
}
