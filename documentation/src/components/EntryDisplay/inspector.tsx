import { useCallback, useState } from "react";
import { blueprints, CustomField, FieldInfo, findEntry, ListField, MapField, ObjectField, Page, PrimitiveField } from "./data";
import { EntryNodeProps } from "./graph";
import { Node, useOnSelectionChange } from "reactflow";
import { format } from ".";
import { Icon } from "@iconify/react/dist/iconify.js";

interface EntryInspectorProps {
    pages: Page[];
}

export default function EntryInspector({ pages }: EntryInspectorProps) {
    const [selectedNodes, setSelectedNodes] = useState<Node<EntryNodeProps, string>[]>([]);

    const onChange = useCallback(({ nodes }: { nodes: Node<EntryNodeProps, string>[] }) => {
        setSelectedNodes(nodes);
    }, [])

    useOnSelectionChange({
        onChange
    });

    return <div className="sm:h-[35em] w-full sm:w-80 md:w-96 p-4 overflow-y-auto">
        {selectedNodes.length === 0 && <EmptyInspector />}
        {selectedNodes.length === 1 && <SingleNodeInspector node={selectedNodes[0]} pages={pages} />}
        {selectedNodes.length > 1 && <MultipleNodesInspector />}
    </div>;
}

function EmptyInspector() {
    return <div className="">
        <div className="text-xl font-bold pb-2">
            Interactive Graph
        </div>
        <p className="text-gray-500 dark:text-gray-400 text-sm">
            This is an interactive graph of all the entries in the selected pages.
            <br />
            You can view different pages by clicking on the tabs.
            Each page contains a view of the entries in that page.
            <br />
            <span className="font-semibold">
                Click on an entry to view its details.
            </span>
        </p>
    </div>;
}

function MultipleNodesInspector() {
    return <div className="">
        <div className="text-xl font-bold pb-2">
            Multiple Nodes Selected
        </div>
        <p className="text-gray-500 dark:text-gray-400 text-sm">
            The inspector can only show one entry at a time.
            <br />
            <span className="font-semibold">
                Click on an entry to view its details.
            </span>
        </p>
    </div>;
}

function SingleNodeInspector({ node, pages }: { node: Node<EntryNodeProps, string>, pages: Page[] }) {
    const entry = node.data.entry;
    const blueprint = blueprints.get(entry.type);
    if (blueprint == null) return null;

    return <div className="">
        <div className="text-xl font-bold mb-2" style={{ color: blueprint.color }}>
            {format(entry.name)}
        </div>
        <div className="text-sm hover:underline" style={{ color: blueprint.color }}>
            {format(blueprint.name)}
        </div>
        <p className="text-gray-500 dark:text-gray-400 text-xs">
            {blueprint.description}
        </p>

        <ObjectFieldInspector fieldInfo={blueprint.fields} path="" value={entry} pages={pages} ignoreFields={["id", "name"]} />
    </div>;
}

function SimpleValueField({ value, icon }: { value: any, icon: string }) {
    return <div className="rounded-md bg-gray-100 dark:bg-gray-800 p-2 text-gray-500 dark:text-gray-400 text-xs w-full flex items-center">
        <Icon icon={icon} className="w-5 h-5 mr-2" />
        {value}
    </div>;
}

function FieldInspector({ fieldInfo, path, value, pages }: { fieldInfo: FieldInfo, path: string, value: any, pages: Page[] }) {
    const type = fieldInfo.kind;
    if (type === "primitive") {
        return <PrimitiveFieldInspector fieldInfo={fieldInfo as PrimitiveField} value={value} />;
    }
    if (type === "enum") {
        return <EnumFieldInspector value={value} />;
    }
    if (type === "list") {
        return <ListFieldInspector fieldInfo={fieldInfo as ListField} path={path} value={value} pages={pages} />;
    }
    if (type === "map") {
        return <MapFieldInspector fieldInfo={fieldInfo as MapField} path={path} value={value} pages={pages} />;
    }
    if (type === "object") {
        return <ObjectFieldInspector fieldInfo={fieldInfo as ObjectField} path={path} value={value} pages={pages} />;
    }
    if (type === "custom") {
        return <CustomFieldInspector fieldInfo={fieldInfo as CustomField} path={path} value={value} pages={pages} />;
    }
    return <div className="text-red-500 dark:text-red-400 text-xs">
        Unknown field type: {type}
    </div>;
}

function PrimitiveFieldInspector({ fieldInfo, value }: { fieldInfo: PrimitiveField, value: any }) {
    if (fieldInfo.type === "boolean") {
        return <div className="text-gray-500 dark:text-gray-400 text-xs w-full flex items-center">
            <input type="checkbox" checked={value} readOnly className="mr-2 accent-green-500" />
            {value ? <span className="text-green-500 dark:text-green-400">true</span> : <span className="text-red-500 dark:text-red-400">false</span>}
        </div>;
    }

    let icon: string
    if (fieldInfo.type === "string") {
        icon = "mdi:text-box-outline";
    } else if (fieldInfo.type === "integer" || fieldInfo.type === "double") {
        icon = "fa6-solid:hashtag";
    } else if (fieldInfo.type === "boolean") {
        icon = "mdi:checkbox-marked-circle-outline";
    }
    return <SimpleValueField value={value} icon={icon} />;
}

function EnumFieldInspector({ value }: { value: any }) {
    return <SimpleValueField value={value} icon="fa-solid:list" />;
}

function ListFieldInspector({ fieldInfo, path, value, pages }: { fieldInfo: ListField, path: string, value: any[], pages: Page[] }) {
    const pathParts = path.split(".");
    const lastPathPart = pathParts[pathParts.length - 1];
    return <div className="">
        <div className="text-gray-500 dark:text-gray-400 text-xs pl-2 space-y-3">
            {value.length === 0 && <div className="text-gray-500 dark:text-gray-400 text-xs">No {lastPathPart} found</div>}
            {value.map((item, index) => <div key={index} className="flex flex-col space-y-1">
                <div className="w-full text-sm">{format(lastPathPart)} ({index})</div>
                <div className="w-full text-sm">
                    <FieldInspector fieldInfo={fieldInfo.type} path={`${path}.${index}`} value={item} pages={pages} />
                </div>
            </div>)}
        </div>
    </div>;
}

function MapFieldInspector({ fieldInfo, path, value, pages }: { fieldInfo: MapField, path: string, value: any, pages: Page[] }) {
    const keyType = fieldInfo.key.kind;
    const valueType = fieldInfo.value.kind;
    if (keyType === "primitive" && valueType === "primitive") {
        return <div className="text-gray-500 dark:text-gray-400 text-xs">
            {path}: {value}
        </div>;
    }
    return <div className="text-gray-500 dark:text-gray-400 text-xs">
        {path}: {value}
    </div>;
}

function ObjectFieldInspector({ fieldInfo, path, value, pages, ignoreFields = [] }: { fieldInfo: ObjectField, path: string, value: any, pages: Page[], ignoreFields?: string[] }) {
    const fields = Object.entries(fieldInfo.fields).filter(([key]) => !ignoreFields?.includes(key));
    const seperator = path.length > 0 ? `.` : "";
    return <div className={`text-gray-700 dark:text-gray-300 text-xs space-y-3 ${path.length > 0 ? "pl-2" : ""}`}>
        {fields.map(([key, field]) => <div key={key} className="flex flex-col space-y-1">
            <div className="w-full text-sm">{format(key)}</div>
            <div className="w-full text-sm">
                <FieldInspector fieldInfo={field} path={`${path}${seperator}${key}`} value={value[key]} pages={pages} />
            </div>
        </div>)}
    </div>;
}

function CustomFieldInspector({ fieldInfo, path, value, pages }: { fieldInfo: CustomField, path: string, value: any, pages: Page[] }) {
    const editor = fieldInfo.editor;
    if (editor === "entryReference") {
        return <EntryReferenceField value={value} pages={pages} />;
    }
    if (editor === "item") {
        return <ItemField {...value} />;
    }
    return <div className="text-red-500 dark:text-red-400 text-xs">
        Unknown custom editor: {editor}
    </div>;
}

function EntryReferenceField({ value, pages }: { value: any, pages: Page[] }) {
    const entry = findEntry(pages, value);
    if (entry == null) return <div className="text-red-500 dark:text-red-400 text-xs">
        Could not find entry with id {value}
    </div>;
    const blueprint = blueprints.get(entry.type);
    if (blueprint == null) return <div className="text-red-500 dark:text-red-400 text-xs">
        Could not find blueprint for entry with id {value}
    </div>;

    return <div className="text-white text-xs rounded-md p-2" style={{ backgroundColor: blueprint.color }}>
        <div className="flex items-center">
            <Icon icon={blueprint.icon} className="w-5 h-5 mr-2" />
            <div className="flex flex-col items-start">
                <span className="text-sm">
                    {format(entry.name)}
                </span>
                <span className="text-[10px] opacity-70 m-0">
                    {format(blueprint.name)}
                </span>
            </div>
        </div>
    </div>;
}

interface ItemFieldProps {
    material: ItemProperty<string>;
    amount: ItemProperty<number>;
    name: ItemProperty<string>;
    lore: ItemProperty<string>;
    flags: ItemProperty<string[]>;
    nbt: ItemProperty<string>;
}

interface ItemProperty<T> {
    enabled: boolean;
    value: T;
}

function ItemField(props: ItemFieldProps) {
    const anyEnabled = Object.values(props).some((prop) => prop.enabled);
    return <div className="pl-2">
        {!anyEnabled && <div className="text-gray-500 dark:text-gray-400 text-xs">No item data found</div>}
        {props.material.enabled && <div>
            Material
            <SimpleValueField value={props.material.value} icon="mdi:cube-outline" />
        </div>}
        {props.amount.enabled && <div>
            Amount
            <SimpleValueField value={props.amount.value} icon="fa6-solid:hashtag" />
        </div>}
        {props.name.enabled && <div>
            Name
            <SimpleValueField value={props.name.value} icon="mdi:text-box-outline" />
        </div>}
        {props.lore.enabled && <div>
            Lore
            <SimpleValueField value={props.lore.value} icon="mdi:text-box-outline" />
        </div>}
        {props.flags.enabled && <div>
            Flags
            <SimpleValueField value={props.flags.value} icon="mdi:checkbox-marked-circle-outline" />
        </div>}
        {props.nbt.enabled && <div>
            NBT
            <SimpleValueField value={props.nbt.value} icon="mdi:text-box-outline" />
        </div>}
    </div>;
}
