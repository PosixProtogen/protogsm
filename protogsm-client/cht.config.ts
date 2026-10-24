/**
 * The CHT client config module
 * This module is responsible for exporting the client config, optionally from .env.
 */

import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const CLIENT_DIR = path.dirname(fileURLToPath(import.meta.url));

/**
 * Reads KEY=VALUE pairs from a .env file
 * @param {string} filePath The .env path
 * @returns {Record<string, string>} The parsed keys
 */
function readDotEnv(filePath: string): Record<string, string> {
    if (!fs.existsSync(filePath)) {
        return {};
    }

    const parsed: Record<string, string> = {};

    for (const rawLine of fs.readFileSync(filePath, "utf8").split(/\r?\n/)) {
        const line = rawLine.trim();

        if (!line || line.startsWith("#")) {
            continue;
        }

        const separator = line.indexOf("=");

        if (separator < 1) {
            continue;
        }

        const key = line.slice(0, separator).trim();
        let value = line.slice(separator + 1).trim();

        if (
            (value.startsWith("\"") && value.endsWith("\"")) ||
            (value.startsWith("'") && value.endsWith("'"))
        ) {
            value = value.slice(1, -1);
        }

        parsed[key] = value;
    }

    return parsed;
}

const env = {
    ...readDotEnv(path.join(CLIENT_DIR, ".env.example")),
    ...readDotEnv(path.join(CLIENT_DIR, ".env"))
};

const apiDev = env.CHT_API_DEV ?? "http://127.0.0.1:3001";

export default {
    name: "protogsm",
    siteTitle: "Protogsm",
    apiBaseUrl: apiDev,
    api: {
        dev: apiDev,
        web: env.CHT_API_WEB ?? apiDev,
        electron: env.CHT_API_ELECTRON ?? apiDev,
        mobile: env.CHT_API_MOBILE ?? apiDev
    },
    apiPortScanLimit: 20,
    devTools: true
};
