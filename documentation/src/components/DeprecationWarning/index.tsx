import React from 'react';
import Admonition from '@theme/Admonition';

interface DeprecationWarningProps {
    adapter: boolean;
    message: string;
}

export default function DeprecationWarning(
    props: DeprecationWarningProps
) {
    return (
        <Admonition type="danger" title="Deprecation Warning">
            <p>
                This {props.adapter ? 'Adapter' : 'Entry'} has been marked as deprecated and will be removed in a future release.
            </p>
            <p>
                Suggested solution:&nbsp;
                <code>{props.message}</code>
            </p>
        </Admonition>
    );
}
