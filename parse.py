# This file is used for parsing TypeWriter adapter's source code and generating markdown files for the documentation website.
# Please note that this code is not perfect and may require some manual editing to get the markdown files to look right.
# Edit the variables below to use this script.
# (Not to mention this code is a rat's nest. Optimize at your own risk.)

import os
import re

# Change these variables to match your setup

adapterName = "[adapter name]"
adapterDesc = "[adapter description]"  # Allows markdown

outPathBase = r"path\to\docs\adapters\[adapter name]"
entryPath = r"path\to\TypeWriter\adapters\[adapter name]\src\...\entries]"

# End of variables


def titleCase(str):
    return " ".join([word[0].upper() + word[1:] for word in str.split(" ")])


def titleCaseSpaced(str):
    newStr = " ".join([word[0].upper() + word[1:] for word in str.split("_")])
    return re.sub('([A-Z])', r' \1', newStr).strip()


def plural(str):
    if str.endswith("y"):
        return str[:-1] + "ies"
    elif str.endswith("s"):
        return str
    return str + "s"


def getEntryData(data, root, file):
    entryData = {}

    # Section
    entryData["section"] = titleCase(
        root.replace(entryPath, "").split("\\")[-1])

    if "Gate" in entryData["section"]:
        entryData["section"] = "Action"
    if "Entities" in entryData["section"]:
        entryData["section"] = "Speaker"

    # Fields
    entryData["fields"] = []
    for i, line in enumerate(data.splitlines()):
        if line.startswith("@Entry("):
            entryData["name"] = titleCaseSpaced(
                file).replace(".kt", "").replace(" Entry", "")
            entryData["description"] = line.split('"')[3].replace(
                f"[{adapterName.replace(' ', '').replace('Adapter', '')}] ", "")

        if line.startswith("	override val") or line.startswith("    override val"):
            line = line.replace("override ", "")
            field = getField(data, line, i)
            if not field:
                continue
            entryData["fields"].append(field)
            continue

        if line.startswith("	val") or line.startswith("    val"):
            field = getField(data, line, i)
            if not field:
                continue
            entryData["fields"].append(field)
            continue

        if line.startswith("	private val") or line.startswith("    private val"):
            line = line.replace("private ", "")
            field = getField(data, line, i)
            if not field:
                continue
            entryData["fields"].append(field)
            continue

        if line.startswith(") :"):
            break

    if not entryData:
        return None
    return entryData


def getField(data, line, i):
    field = {}

    field["name"] = line.strip().split(" ")[1].replace(":", "")
    if (field["name"] == "id" or field["name"] == "name"):
        return None

    field["name"] = titleCaseSpaced(field["name"])
    field["name"] = field["name"].strip()

    field["type"] = line.strip().split(" ")[2].replace("Optional", "").replace("List", "").replace(
        "<", "").replace(">", "").replace(",", "")
    field["optional"] = "Optional" in line

    if ("Trigger" in field["name"]):
        if ("Custom" in field["name"]):
            field["name"] = "Triggers"
        field["type"] = "Trigger"
    elif ("Speaker" in field["name"]):
        field["type"] = "Speaker"

    desc = data.splitlines()[i-1]
    field["description"] = ""
    if "@Help" in desc:
        field["description"] = desc.split('"')[1]
    else:
        if "Trigger" in field["name"]:
            return None
        elif "Criteria" in field["name"]:
            return None
        elif "Modifiers" in field["name"]:
            return None
        elif "Display Name" in field["name"]:
            return None
        elif "Speaker" in field["name"]:
            field["description"] = "The speaker of the dialogue"
        elif "Sound" in field["name"] and field["description"] == "":
            return None
        elif "Command" in field["name"] and field["description"] == "":
            field["description"] = "The command to register. Do not include the leading slash."
        elif "Comment" in field["name"] and field["description"] == "":
            return None
        elif field["description"] == "":
            field["description"] = "No description provided"
            print("No description found for field in " + line.split(" ")
                  [1].replace(":", "") + " (" + field["name"] + ")")

    return field


def createMarkdown(data, root, file):
    entryData = getEntryData(data, root, file)
    if not entryData:
        print("No entry data found")
        return None
    markdown = f"""import {{{plural(entryData['section'])}Field, EntryField}} from "@site/src/components/EntryField";

# {entryData['name']}

{entryData['description']}.

## How could this be used?



## Fields

<{plural("Action" if "Dialogue" in entryData["section"] else entryData["section"])}Field />
"""
    for field in entryData["fields"]:
        markdown += f"""
<EntryField name="{field["name"]}"{" optional" if field["optional"] else " required"}>
    {field["description"]}
</EntryField>
"""
    return markdown


def main():
    skipFileExistsCheck = False

    if not os.path.exists(outPathBase):
        os.makedirs(outPathBase)
    if not os.path.exists(os.path.join(outPathBase, "entries")):
        os.makedirs(os.path.join(outPathBase, "entries"))

    text = f"""# {adapterName}
{adapterDesc}

## Entries
"""
    sectionsWritten = []

    with open(os.path.join(outPathBase, "_category_.yml"), "w") as f:
        f.write(f"label: {adapterName}")

    with open(os.path.join(outPathBase + r"\entries", "_category_.yml"), "w") as f:
        f.write(f"label: Entries")

    for root, dirs, files in os.walk(entryPath):
        if "messengers" in root:
            continue
        for file in files:
            outputPath = root.replace(entryPath, outPathBase + r"\entries").replace(
                "entities", "speaker").replace("gate", "action")
            outputFile = os.path.join(outputPath, file.replace(
                ".kt", ".mdx").replace("Entry", ""))

            if not os.path.exists(outputPath):
                os.makedirs(outputPath)

            with open(os.path.join(root, file), "r") as f:
                data = f.read()
                print("Parsing " + file)
                try:
                    markdown = createMarkdown(data, root, file)
                except Exception as e:
                    print(f"Failed to parse {file} ({e})")
                    continue
                if markdown:
                    with open(os.path.join(outPathBase, adapterName.replace(" ", "") + ".md"), "w") as f:
                        entry = getEntryData(data, root, file)

                        with open(os.path.join(outputPath, "_category_.yml"), "w") as f:
                            f.write(
                                f"label: {plural(titleCaseSpaced(entry['section']))}\n")

                        if entry["section"] not in sectionsWritten:
                            text += f"""
### {entry["section"]}

| Name | Description |
| ---- | ----------- |"""
                            sectionsWritten.append(entry["section"])

                        text += f"""
| [{entry["name"]}]({adapterName.replace(" ", "")}/entries/{entry["section"].lower()}/{entry["name"].replace(" ", "")}) | {entry["description"]} |"""
                    try:
                        if not os.path.exists(outputFile):
                            with open(outputFile, "w") as f:
                                f.write(markdown)
                        else:
                            if (not skipFileExistsCheck):
                                yn = input(
                                    f"File already exists: {file}. Replace? (Y/n/all) ")
                            else:
                                yn = "y"
                                print(
                                    f"File already exists: {file}, replacing")
                            if yn.lower() == "y":
                                pass
                            elif yn.lower() == "all":
                                skipFileExistsCheck = True
                            elif yn.lower() == "":
                                print("Skipping file")
                                continue
                            else:
                                print("Skipping file")
                                continue
                            with open(outputFile, "w") as f:
                                f.write(markdown)
                    except Exception as e:
                        print(
                            f"Error writing {file}, putting at base directory ({e})")
                        with open(os.path.join(outPathBase, file.replace(".kt", ".mdx").replace("Entry", "")), "w") as f:
                            f.write(markdown)
                        continue
                    print("")
    with open(os.path.join(outPathBase, adapterName.replace(" ", "") + ".md"), "w") as f:
        f.write(text)


if __name__ == "__main__":
    main()
