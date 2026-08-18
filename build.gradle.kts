val modId: String by project
val modName: String by project
val modDescription: String by project
val modAuthor: String by project
val modVersion: String by project
val modLicense: String by project
val minecraftVersion: String by project
val fabricVersion: String by project
val mixinVersion: String by project

plugins {
	id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
	id("maven-publish")
}

repositories {
	mavenCentral()
}

base {
	archivesName.set(modId)
}

version = "$minecraftVersion+v$modVersion"
group = "dev.whisperlyric.someshitleakfix"

dependencies {
	minecraft("com.mojang:minecraft:$minecraftVersion")
	implementation("net.fabricmc:fabric-loader:$fabricVersion")

	compileOnly(files("libs/fabric-carpet-26.1+v260402.jar"))
	compileOnly(files("libs/carpet-org-addition-mc26.1.x-v1.44.0-2604242113.jar"))

	compileOnly("net.fabricmc:sponge-mixin:$mixinVersion")
}

tasks.processResources {
	inputs.property("version", project.version)
	inputs.property("fabricVersion", fabricVersion)
	inputs.property("minecraftVersion", minecraftVersion)

	filesMatching("fabric.mod.json") {
		expand(
			"version" to project.version,
			"fabricVersion" to fabricVersion,
			"minecraftVersion" to minecraftVersion
		)
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	options.release.set(25)
}

java {
	withSourcesJar()

	toolchain {
		languageVersion.set(JavaLanguageVersion.of(25))
	}
}

tasks.jar {
	from("LICENSE") {
		rename { "${it}_${base.archivesName.get()}" }
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}
}
