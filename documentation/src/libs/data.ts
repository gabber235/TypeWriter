import adapters from "./adapters.json";

export const blueprints = new Map((adapters as Adapter[]).flatMap((adapter) => adapter.entries).map((entry) => [entry.name, entry]));

export interface Adapter {
    name: string;
    description: string;
    version: string;
    entries: Blueprint[];
}

export interface Blueprint {
    name: string;
    description: string;
    adapter: string;
    fields: ObjectField;
    tags: string[];
    color: string;
    icon: string;
}

export interface Modifier {
    name: string;
    data?: any;
}

export function blueprintFieldsWithModifier(blueprint: Blueprint, modifier: string): Map<string, Modifier> {
    return fieldsWithModifier(blueprint.fields, modifier, "");
}

export function fieldsWithModifier(field: FieldInfo, modifierName: string, path: string): Map<string, Modifier> {
    const modifier = getModifier(field, modifierName);
    const modifiers = new Map<string, Modifier>();
    if (modifier != null) {
        modifiers.set(path, modifier);
    }

    const seperator = path.length > 0 ? "." : "";

    if (field.kind === "object") {
        const objectField = field as ObjectField;
        for (const [key, value] of Object.entries(objectField.fields)) {
            fieldsWithModifier(value, modifierName, `${path}${seperator}${key}`).forEach((m, p) => modifiers.set(p, m));
        }
    } else if (field.kind === "list") {
        const listField = field as ListField;
        fieldsWithModifier(listField.type, modifierName, `${path}${seperator}*`).forEach((m, p) => modifiers.set(p, m));
    } else if (field.kind === "map") {
        const mapField = field as MapField;
        fieldsWithModifier(mapField.value, modifierName, `${path}${seperator}*`).forEach((m, p) => modifiers.set(p, m));
    }

    return modifiers;
}

export function getModifier(field: FieldInfo, modifierName: string): Modifier | undefined {
    return field.modifiers.find((m) => m.name.startsWith(modifierName));
}

export interface FieldInfo {
    kind: string;
    modifiers: Modifier[];
}

export interface PrimitiveField extends FieldInfo {
    kind: 'primitive';
    type: 'string' | 'integer' | 'boolean' | 'double';
}

export interface EnumField extends FieldInfo {
    kind: 'enum';
    values: string[];
}

export interface ListField extends FieldInfo {
    kind: 'list';
    type: FieldInfo;
}

export interface MapField extends FieldInfo {
    kind: 'map';
    key: FieldInfo;
    value: FieldInfo;
}

export interface ObjectField extends FieldInfo {
    kind: 'object';
    fields: { [key: string]: FieldInfo };
}

export interface CustomField extends FieldInfo {
    kind: 'custom';
    editor: string;
    default: any;
    fieldInfo?: FieldInfo;
}


export interface Page {
    name: string;
    type: "sequence" | "static" | "cinematic" | "manifest";
    entries: Entry[];
    chapter: string;
    priority: number;
}

export function findEntry(pages: Page[], id: string): Entry | undefined {
    return pages.flatMap((page) => page.entries).find((entry) => entry.id === id);
}

export interface Entry {
    id: string;
    name: string;
    type: string;
    [key: string]: any;
}

function crawl(data: any, path: string): any[] {
    if (path === '') {
        return [data];
    }

    const parts = path.split('.');
    return parts.reduce((current: any[], part: string) => {
        return current.flatMap((item: any) => {
            if (Array.isArray(item)) {
                return crawlInList(item, part);
            } else if (typeof item === 'object' && item !== null) {
                return crawlInMap(item, part);
            }
            return [];
        });
    }, [data]);
}

function crawlInMap(map: Record<string, any>, part: string): any[] {
    if (part === '*') {
        return Object.values(map);
    } else if (part in map) {
        return [map[part]];
    }
    return [];
}

function crawlInList(list: any[], part: string): any[] {
    if (part === '*') {
        return list;
    } else {
        const index = parseInt(part, 10);
        if (!isNaN(index) && index >= 0 && index < list.length) {
            return [list[index]];
        }
    }
    return [];
}

export function getAll(entry: Entry, path: string): any[] {
    return crawl(entry, path);
}

export function get(entry: Entry, path: string, defaultValue?: any): any {
    const result = crawl(entry, path);
    return result.length > 0 ? result[0] : defaultValue;
}
