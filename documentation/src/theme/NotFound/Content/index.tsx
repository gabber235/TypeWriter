import React from "react";
import clsx from "clsx";
import type { Props } from "@theme/NotFound/Content";

export default function NotFoundContent({ className }: Props): JSX.Element {
  return (
    <main
      className={clsx(
        "container mx-auto flex flex-col items-center justify-center min-h-screen text-center",
        className
      )}
    >
      <div className="flex flex-col lg:flex-row items-center justify-center w-full lg:max-w-4xl">
        <div className="w-full lg:w-1/2 flex justify-center">
          <img src="/img/404.svg" className="w-full max-w-md" />
        </div>
        <div className="w-full lg:w-1/2 text-center lg:text-left mt-12 lg:mt-0 lg:pl-12">
          <h1 className="text-6xl font-bold mb-6">404</h1>
          <h2 className="text-3xl mb-6">UH OH! You're lost.</h2>
          <p className="text-lg mb-5  ">
            We apologize, but the page you are trying to access cannot be found.
            <br></br> Please check the URL for errors or use the button below to return to the homepage.
            <br></br>If you believe this is an error, please create a question in our <a href="https://discord.gg/gs5QYhfv9x">Discord</a>.
          </p>
          <a href="/">
            <button className="button px-6 py-3 text-lg font-semibold text-white bg-[#2ca1cc] rounded-lg hover:bg-[#227c9d] border-[#227c9d] duration-200 transition">
              GO TO HOME
            </button>
          </a>
        </div>
      </div>
    </main>
  );
}
