import ReactFlow, {Background,BackgroundVariant,Controls,NodeProps,Node,Edge,SelectionMode,useEdgesState,useNodesState,Handle,Position,useReactFlow,} from "reactflow";
import Dagre, { GraphLabel } from "@dagrejs/dagre";
import "reactflow/dist/style.css";
import { useEffect } from "react";
import {blueprintFieldsWithModifier,blueprints,Entry,getAll,Page,} from "../../libs/data";
import { format } from ".";
import { Icon } from "@iconify/react";

function getEdges(
    entry: Entry,
    acceptedTag: string,
    entries: Entry[]
): NodeEdge[] {
    const edges: NodeEdge[] = [];
    const blueprint = blueprints.get(entry.type);
    if (blueprint == null) return edges;

    const modifiers = blueprintFieldsWithModifier(blueprint, "entry");

    const references = [];
    modifiers.forEach((_, path) => {
        references.push(...getAll(entry, path));
    });

    const validReferences = references.filter((reference) => {
        const referenceEntry = entries.find((e) => e.id === reference);
        if (referenceEntry == null) return false;
        const referenceBlueprint = blueprints.get(referenceEntry.type);
        if (referenceBlueprint == null) return false;
        return referenceBlueprint.tags.includes(acceptedTag);
    });

    return validReferences.map((reference) => {
        return {
            id: `${entry.id}-${reference}`,
            source: entry.id,
            target: reference,
            style: {
                stroke: blueprint.color,
                strokeWidth: 3,
            },
        };
    });
}

const g = new Dagre.graphlib.Graph().setDefaultEdgeLabel(() => ({}));

interface NodeEdge {
    id: string;
    source: string;
    target: string;
    style?: React.CSSProperties;
}

function getLayoutedElements(
    nodes: Node<EntryNodeProps, string>[],
    edges: Edge<any>[],
    options: GraphLabel
): { nodes: Node<EntryNodeProps, string>[]; edges: Edge<any>[] } {
    g.setGraph({ rankdir: options.rankdir });

    edges.forEach((edge) => g.setEdge(edge.source, edge.target));
    nodes.forEach((node) => g.setNode(node.id, node));

    Dagre.layout(g);

    return {
        nodes: nodes.map((node) => {
            const position = g.node(node.id);
            // We are shifting the dagre node position (anchor=center center) to the top left
            // so it matches the React Flow node anchor point (top left).
            const x = position.x - (node.width !== undefined ? node.width / 2 : 0);
            const y = position.y - (node.height / 2 || 0);

            if (isNaN(x) || isNaN(y)) return node;

            return { ...node, position: { x, y } };
        }),
        edges,
    };
}

const nodeTypes = { custom: EntryNode };

interface PageLayoutProps {
    page: Page;
}

export function PageLayout({ page }: PageLayoutProps) {
    const tag = page.type === "manifest" ? "manifest" : "triggerable";
    const direction = page.type === "manifest" ? "TB" : "LR";

    const { fitView } = useReactFlow();
    const [nodes, setNodes, onNodesChange] = useNodesState(
        page.entries.map((entry) => ({
            id: entry.id,
            type: "custom",
            position: { x: 0, y: 0 },
            data: { entry, direction } as EntryNodeProps,
        }))
    );
    const [edges, setEdges, onEdgesChange] = useEdgesState(
        page.entries.flatMap((entry) => getEdges(entry, tag, page.entries))
    );

    const hasSizes = nodes.some(
        (node) => node.width !== undefined && node.height !== undefined
    );

    useEffect(() => {
        const { nodes: layoutedNodes, edges: layoutedEdges } = getLayoutedElements(
            nodes,
            edges,
            { rankdir: direction }
        );
        setNodes(layoutedNodes);
        setEdges(layoutedEdges);

        // We wait twice since the nodes are not immediately updated.
        window.requestAnimationFrame(() => {
            window.requestAnimationFrame(() => {
                fitView({ padding: 0.2 });
            });
        });
    }, [direction, hasSizes]);

    return (
        <div className="h-[35em] w-full">
            <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                nodeTypes={nodeTypes}
                selectionMode={SelectionMode.Partial}
                fitView
                proOptions={{ hideAttribution: true }}
            >
                <Controls />
                <Background variant={BackgroundVariant.Dots} gap={12} size={1} />
            </ReactFlow>
        </div>
    );
}

export interface EntryNodeProps {
    entry: Entry;
    direction: "TB" | "LR";
}
function EntryNode({ data, selected }: NodeProps<EntryNodeProps>) {
    const entry = data.entry;
    const direction = data.direction;
    const blueprint = blueprints.get(entry.type);
    if (blueprint == null) return null;

    return (
        <>
            <div
                className="text-white rounded-md p-[0.3rem]"
                style={{ backgroundColor: blueprint.color }}
            >
                <div
                    className="flex items-center border-3 border-solid border-white dark:border-black rounded-sm"
                    style={{
                        borderStyle: selected ? "solid" : "none",
                        padding: selected ? "0.3rem" : "0.5rem",
                    }}
                >
                    <Icon icon={blueprint.icon} className="w-5 h-5 mr-2" />
                    <div className="flex flex-col">
                        {format(entry.name)}
                        <div className="text-xs text-white opacity-70 font-extrabold">
                            {format(entry.type)}
                        </div>
                    </div>
                </div>
            </div>
            <Handle
                type="target"
                position={direction === "TB" ? Position.Top : Position.Left}
                className="opacity-0"
            />
            <Handle
                type="source"
                position={direction === "TB" ? Position.Bottom : Position.Right}
                className="opacity-0"
            />
        </>
    );
}
