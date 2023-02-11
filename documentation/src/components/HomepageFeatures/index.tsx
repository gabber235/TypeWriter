import clsx from 'clsx';
import React from 'react';
import styles from './styles.module.css';

type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: JSX.Element;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'Easy to use',
    Svg: require('@site/static/img/undraw_financial_data_re_p0fl.svg').default,
    description: (
      <>
        TypeWriter is a block-based building tool that allows players to design gameplay without need to write any code.
        For more complex use cases, it provides very straight forward API.
      </>
    ),
  },
  {
    title: 'Awesome experience',
    Svg: require('@site/static/img/undraw_code_thinking_re_gka2.svg').default,
    description: (
      <>
        Instead coding or writing complex YAML configurations, it uses a simple, intuitive web interface that allows everyone to create unique mechanics in no time.
      </>
    ),
  },
  {
    title: 'Welcoming community',
    Svg: require('@site/static/img/undraw_collaborators_re_hont.svg').default,
    description: (
      <>
        Our community is awesome and helpful. It provides inclusive evironment for people to share their creations, exchange tips and tricks, and collaborate on building projects
      </>
    ),
  },
];

function Feature({title, Svg, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
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
