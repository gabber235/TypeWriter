import React from "react";
import styles from "./styles.module.css";
import Badge from "@site/src/components/Badges";

interface EntryFieldProps {
    name: string;
    children?: React.ReactNode;
    required?: boolean;
    inherited?: boolean;
    optional?: boolean;
    deprecated?: boolean;
    colored?: boolean;
    placeholders?: boolean;
    regex?: boolean;
    duration?: boolean;
    multiple?: boolean;
}

export const RequiredBadge = () => <Badge name="Required" color="#ff3838" />;
export const InheritedBadge = () => <Badge name="Inherited" color="#a83dff" />;
export const OptionalBadge = () => <Badge name="Optional" color="#3191f7" />;
export const DeprecatedBadge = () => <Badge name="Deprecated" color="#fa9d2a" />;
export const ColoredBadge = () => <Badge name="Colored" color="#ff8e42" />;
export const PlaceholdersBadge = () => <Badge name="Placeholders" color="#00b300" />;
export const RegexBadge = () => <Badge name="Regex" color="#00b300" />;
export const MultipleBadge = () => <Badge name="List" color="#00b300" />;

export const EntryField = (props: EntryFieldProps) => {
    return (
        <div className={styles.entryField}>
            <div className={styles.header}>
                <h2 className={styles.name}>{props.name}</h2>
                {props.required && <RequiredBadge />}
                {props.inherited && <InheritedBadge />}
                {props.optional && <OptionalBadge />}
                {props.deprecated && <DeprecatedBadge />}
                {props.colored && <ColoredBadge />}
                {props.placeholders && <PlaceholdersBadge />}
                {props.regex && <RegexBadge />}
                {props.multiple && <MultipleBadge />}
            </div>
            <div className="">
                {props.children}
                {props.colored && <ColorInfo />}
                {props.placeholders && <PlaceholderInfo />}
                {props.regex && <RegexInfo />}
                {props.duration && <DurationInfo />}
            </div>
        </div>
    );
};

export const CriteriaField = () => {
    return (
        <EntryField name="Criteria" inherited multiple>
            A list of facts that must be met by the player before this entry can be triggered.
        </EntryField>
    );
};
export const ModifiersField = () => {
    return (
        <EntryField name="Modifiers" inherited multiple>
            A list of facts that will be modified for the player when this entry is triggered.
        </EntryField>
    );
};

export const TriggersField = () => {
    return (
        <EntryField name="Triggers" inherited multiple>
            A list of entries that will be triggered after this entry is triggered.
        </EntryField>
    );
};

export const ActionsField = () => {
    return (
        <div>
            <CriteriaField />
            <ModifiersField />
            <TriggersField />
        </div>
    );
};

export const FactsField = () => {
    return (
        <div>
            <EntryField name="Comment" optional inherited>
                A comment to keep track of what this fact is used for.
            </EntryField>
        </div>
    );
};

export const EventsField = () => {
    return (
        <div>
            <TriggersField />
        </div>
    );
};

export const SpeakersField = () => {
    return (
        <div>
            <EntryField name="Display Name" required inherited>
                The display name of the speaker.
            </EntryField>
            <EntryField name="Sound" required inherited>
                The sound that will be played when the speaker speaks.
            </EntryField>
        </div>
    );
};

export const ColorInfo = () => {
    return (
        <div>
            <br />
            Colors and formatting from the{" "}
            <a href="https://docs.advntr.dev/minimessage/format.html">
                <code>MiniMessage Adventure Api</code>
            </a>{" "}
            can be used. So for example, you can use <code>&lt;red&gt;Some Text&lt;/red&gt;</code> for red text.
        </div>
    );
};

export const PlaceholderInfo = () => {
    return (
        <div>
            <br />
            Placeholders from the <code>PlaceholderApi</code> can be used. So for example, you can use <code>%player_name%</code> for the player name.
        </div>
    );
};

export const DurationInfo = () => {
    return (
        <div>
            <br />
            Durations can be specified in the following format: <code>1d 2h 3m 4s</code>. The following units are supported: <code>d</code> for days, <code>h</code> for hours,
            <code>m</code> for minutes and <code>s</code> for seconds.
        </div>
    );
};

export const RegexInfo = () => {
    return (
        <div>
            <br />
            Regular expressions can be used to match a pattern. For example, <code>^.*$</code> will match any string.
        </div>
    );
};
