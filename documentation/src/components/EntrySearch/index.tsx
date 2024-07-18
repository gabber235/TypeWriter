import React from 'react';
import { Icon } from '@iconify/react';
import { blueprints } from '@site/src/libs/data';
import Admonition from '@theme/Admonition';

interface EntryProps {
    entryName: string;
}
const EntrySearch: React.FC<EntryProps> = ({ entryName }) => {
    const parsedEntry = blueprints.get(entryName);

    if (!parsedEntry) {
        return <div className="bg-red-500 text-white p-4 rounded">Invalid entry, please report this in the TypeWriter Discord. {entryName}</div>;;
    }

    const { name, description, color, icon } = parsedEntry;
    const finalName = name.replace(/_/g, ' ').replace(/\b\w/g, (c) => c.toUpperCase());
    return (
        <div className="w-full h-full flex justify-center items-center select-none pt-5 pb-10">
            <div className="bg-[#171719] p-3 rounded-[12px] select-none">
                <div className="flex items-center mb-4 space-x-2 w-[280px]">
                    <Icon icon="mdi:filter" className="text-white text-2xl" />
                    <div className="flex space-x-2">
                        <button className="flex items-center bg-[#448aff] rounded-full text-white px-3 py-1 text-sm cursor-pointer hover:bg-[#3d7ce5]" disabled>
                            <Icon icon="ic:outline-search" className="text-white text-lg" />
                            <span className="ml-1">New Entries</span>
                        </button>
                        <button className="flex items-center bg-[#448aff] rounded-full text-white px-3 py-1 text-sm cursor-pointer hover:bg-[#3d7ce5]" disabled>
                            <Icon icon="ic:outline-search" className="text-white text-lg" />
                            <span className="ml-1">Add Page</span>
                        </button>
                    </div>
                </div>
                <div className="flex items-center bg-[#141218] rounded-lg px-3 py-2 mb-4 w-96 select-none">
                    <Icon icon="ic:outline-search" className="text-[#e6e0e9] text-2xl" />
                    <input
                        type="text"
                        value={`Add ${finalName}`}
                        className="bg-transparent border-none outline-none flex-grow ml-2 text-[#e6e0e9] text-lg pointer-events-auto cursor-text"
                        readOnly
                    />
                    <Icon icon="mingcute:close-fill" className="text-[#e6e0e9] text-[30px] cursor-pointer hover:bg-[#222026] rounded-full p-1" />
                </div>
                <div className="flex items-center bg-[#141218] rounded-lg cursor-pointer text-white w-96 mt-2 text-lg hover:bg-[#121016]">
                    <div className="flex items-center justify-center mr-2 p-4 rounded" style={{ backgroundColor: color }}>
                        <Icon icon={icon} className="text-[#141218] text-2xl" />
                    </div>
                    <div className="flex-grow">
                        <div className="font-medium text-[16px] text-[#e6e0e9] select-none">Add {finalName}</div>
                        <div className="text-[#a09fa6] text-sm select-none">{description}</div>
                    </div>
                    <span className="text-white text-2xl pr-2.5 select-none">+</span>
                </div>
            </div>
        </div>
    );
};

export default EntrySearch;
