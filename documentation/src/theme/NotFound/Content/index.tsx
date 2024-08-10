import React from "react";
import clsx from "clsx";
import type { Props } from "@theme/NotFound/Content";
import Rive from '@rive-app/react-canvas';

export default function NotFoundContent({ className }: Props): JSX.Element {
    return (
        <main
            className={clsx(
                "container mx-auto flex flex-col items-center justify-center min-h-screen text-center",
                className
            )}
        >
            <div className="flex flex-col lg:flex-row items-center justify-center w-full lg:max-w-5xl spacing-x-12">
                <div className="w-full h-full flex justify-center">
                    <Rive
                        className="w-full h-full aspect-[1644.16/1000]"
                        src={require('@site/static/rive/robot.riv').default}
                        stateMachines="State Machine 1"
                    />
                </div>
                <div className="w-full md:w-2/3 text-left spacing-y-6">
                    <h1 className="text-6xl font-bold">404</h1>
                    <h2 className="text-3xl">UH OH! You're lost.</h2>
                    <p className="text-lg text-gray-500 dark:text-gray-400">
                        We apologize, but the page you are trying to access cannot be found.
                        <br></br> Please check the URL for errors or use the button below to return to the homepage.
                        <br></br>If you believe this is an error, please create a question in our <a href="https://discord.gg/gs5QYhfv9x">Discord</a>.
                    </p>
                    <a href="/">
                        <button className="button px-6 py-3 text-lg font-bold text-white bg-[#2ca1cc] rounded-lg hover:bg-[#227c9d] border-[#227c9d] duration-200 transition">
                            Go Home
                        </button>
                    </a>
                </div>
            </div>
        </main>
    );
}
