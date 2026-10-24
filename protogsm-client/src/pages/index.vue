<template>
    <main class="p-8">
        <h1 class="text-2xl font-semibold">
            {{ $project.labels.siteTitle }}
        </h1>

        <p class="mt-2 text-muted-foreground">
            Home
        </p>

        <section class="mt-8 flex flex-wrap items-center gap-3">
            <Button label="Default" />
            <Button
                variant="primary"
                label="Primary"
            />
            <Button
                variant="secondary"
                label="Secondary"
            />
            <Button
                variant="destructive"
                label="Destructive"
            />
            <Button
                variant="success"
                label="Success"
            />
            <Button
                label="Disabled"
                disabled
            />
        </section>

        <section class="mt-8 grid max-w-md gap-4">
            <Input
                v-model="name"
                type="text"
                label="Nome"
                placeholder="Digite seu nome"
            />
            <Input
                v-model="email"
                type="email"
                label="E-mail"
                placeholder="voce@exemplo.com"
            />
            <Input
                v-model="password"
                type="password"
                label="Senha"
                placeholder="********"
            />
            <Input
                type="text"
                label="Desabilitado"
                placeholder="Não editável"
                disabled
            />
        </section>
    </main>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
    name: "IndexPage",

    data() {
        return {
            name: "",
            email: "",
            password: ""
        };
    }
});
</script>
