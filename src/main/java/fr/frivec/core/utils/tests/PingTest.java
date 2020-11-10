package fr.frivec.core.utils.tests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.frivec.spigot.API;
import fr.frivec.spigot.packets.PacketUtils;

public class PingTest {
	
	private HashMap<Date, Integer> pingsRegistered;
	private UUID uuid;
	
	public PingTest(final Player player) {
		
		this.pingsRegistered = new HashMap<Date, Integer>();
		this.uuid = player.getUniqueId();
		
	}
	
	public void registerPing() {
		
		this.pingsRegistered.put(Date.from(Instant.now()), PacketUtils.getEntityPlayer(Objects.requireNonNull(Bukkit.getPlayer(this.uuid))).ping);
		
	}
	
	public void serialize() throws IOException {
		
		final Path folder = Paths.get(API.getInstance().getDataFolder() + "/tests/"),
					file = Paths.get(folder + "/pingTest-" + this.uuid.toString() + ".json");
		
		if(Files.notExists(folder))
			
			Files.createDirectory(folder);
		
		if(Files.exists(file))
			
			Files.delete(file);
		
		Files.createFile(file);
		
		final String json = API.getInstance().getGsonManager().serializeObject(this);
		
		final BufferedWriter bufferedWriter = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
		
		bufferedWriter.write(json);
		bufferedWriter.flush();
		bufferedWriter.close();
		
	}
	
	public HashMap<Date, Integer> getPingsRegistered() {
		return pingsRegistered;
	}
	
	public UUID getUuid() {
		return uuid;
	}

}
