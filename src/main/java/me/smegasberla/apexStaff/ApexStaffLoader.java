package me.smegasberla.apexStaff;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public class ApexStaffLoader implements PluginLoader {
    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(new RemoteRepository.Builder("codemc", "default", "https://repo.codemc.io/repository/maven-releases/").build());

        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.retrooper:packetevents-spigot:2.11.2"), null));

        classpathBuilder.addLibrary(resolver);

        

    }
}
