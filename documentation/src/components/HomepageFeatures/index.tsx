import clsx from 'clsx';
import React from 'react';
import styles from './styles.module.css';
import Rive from '@rive-app/react-canvas';

type FeatureItem = {
    title: string;
    RiveConfig: {
        artboard: string | undefined;
        stateMachines: string | string[] | undefined;
        animations: string | string[] | undefined;
        src: string;
    };
    description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
    {
        title: 'Easy to use',
        RiveConfig: {
            artboard: undefined,
            stateMachines: 'State Machine 1',
            animations: undefined,
            src: require('@site/static/rive/working.riv').default,
        },
        description: (
            <>
                Instead coding or writing complex YAML configurations, it uses a simple, intuitive web interface that
                allows everyone to create unique mechanics in no time.
            </>
        ),
    },
    {
        title: 'Infinitely customizable',
        RiveConfig: {
            artboard: undefined,
            stateMachines: 'Motion',
            animations: undefined,
            src: require('@site/static/rive/character_cycle.riv').default,
        },
        description: (
            <>
                Typewriter is fully customizable. You can change the look and feel of every part to allow
                for truly unique experiences. Flexibility and extensibility are key.
            </>
        ),
    },
    {
        title: 'Welcoming community',
        RiveConfig: {
            artboard: "All Avatars",
            stateMachines: undefined,
            animations: 'Group Animation',
            src: require('@site/static/rive/community.riv').default,
        },
        description: (
            <>
                Our community is awesome and helpful. It provides inclusive environment for people to share their
                creations, exchange tips and tricks, and collaborate on building projects
            </>
        ),
    },
];

function Feature({ title, RiveConfig, description }: FeatureItem) {
    return (
        <div className={clsx('col col--4', styles.feature)}>
            <div className="text--center">
                <Rive
                    className={styles.featureRive}
                    {...RiveConfig}
                />
            </div>
            <div className="text--center padding-horiz--md">
                <h3>{title}</h3>
                <p>{description}</p>
            </div>
        </div>
    );
}

export default function HomepageFeatures(): JSX.Element {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
