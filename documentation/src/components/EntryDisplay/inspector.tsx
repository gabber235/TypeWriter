import { useCallback, useState } from "react";
import { Page } from "../../libs/data";
import { EntryNodeProps } from "./graph";
import { Node, useOnSelectionChange } from "reactflow";
import { EntryInspector, InspectorContext } from "../EntryInspector";

interface EntryInspectorProps {
    pages: Page[];
}

export default function GraphEntryInspector({ pages }: EntryInspectorProps) {
    const [selectedNodes, setSelectedNodes] = useState<
        Node<EntryNodeProps, string>[]
    >([]);

    const onChange = useCallback(
        ({ nodes }: { nodes: Node<EntryNodeProps, string>[] }) => {
            setSelectedNodes(nodes);
        },
        []
    );

    useOnSelectionChange({
        onChange,
    });

    return (
        <div className="sm:h-[35em] w-full sm:w-80 md:w-96 p-4 overflow-y-auto">
            {selectedNodes.length === 0 && <EmptyInspector />}
            {selectedNodes.length === 1 && (
                <InspectorContext.Provider value={{ defaultExpanded: true }}>
                    <EntryInspector entry={selectedNodes[0].data.entry} pages={pages} />
                </InspectorContext.Provider>
            )}
            {selectedNodes.length > 1 && <MultipleNodesInspector />}
        </div>
    );
}

function EmptyInspector() {
    return (
        <div className="">
            <div className="text-xl font-bold pb-2">Interactive Graph</div>
            <p className="text-gray-500 dark:text-gray-400 text-sm">
                This is an interactive graph of all the entries in the selected pages.
                <br />
                You can view different pages by clicking on the tabs. Each page contains
                a view of the entries in that page.
                <br />
                <span className="font-semibold">
                    Click on an entry to view its details.
                </span>
            </p>
        </div>
    );
}

function MultipleNodesInspector() {
    return (
        <div className="">
            <div className="text-xl font-bold pb-2">Multiple Nodes Selected</div>
            <p className="text-gray-500 dark:text-gray-400 text-sm">
                The inspector can only show one entry at a time.
                <br />
                <span className="font-semibold">
                    Click on an entry to view its details.
                </span>
            </p>
        </div>
    );
}
