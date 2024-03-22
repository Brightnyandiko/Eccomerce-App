pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //noninspection JcenterRepositoryObsolete
        jcenter()
    }
}

rootProject.name = "Ecommerce-App"
include(":app")
