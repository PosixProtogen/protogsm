import type { ProjectState } from "@base/project";

declare module "vue" {
    interface ComponentCustomProperties {
        $project: ProjectState;
    }
}

export {};
