package me.smegasberla.apexStaff.engine.prediction;

// import me.smegasberla.apexStaff.ApexStaff;
// import org.bukkit.Location;
// import org.bukkit.Material;
// import org.bukkit.block.Block;
// import org.bukkit.block.spawner.SpawnerEntry;
// import org.bukkit.enchantments.Enchantment;
// import org.bukkit.entity.Entity;
// import org.bukkit.entity.Player;
// import org.bukkit.event.entity.EntityDamageByEntityEvent;
// import org.bukkit.event.player.PlayerMoveEvent;
// import org.bukkit.inventory.ItemStack;
// import org.bukkit.potion.PotionEffectType;


// import java.util.HashMap;
// import java.util.UUID;

public class PredictionEngine {

//     /*
//     What will the prediction engine detect:
//     - Reach
//     - Jesus
//     - Fly
//     - Speed
//     */

//     public HashMap<UUID, Integer> suspicionPoints = new HashMap<>();
//     public HashMap<UUID, Integer> flyBuffer = new HashMap<>();
//     public HashMap<UUID, Integer> speedBuffer = new HashMap<>();


//     int[] points = {1,2,3,4};

//     public int getSuspicionPoints(UUID uuid) {

//         int pt = suspicionPoints.getOrDefault(uuid, 0);

//         return pt;

//     }

//     public void addSuspiciusPoints(UUID uuid, Integer points) {

//         suspicionPoints.putIfAbsent(uuid, points);

//     }


//     public void removePlayerFromSuspicion(UUID uuid) {

//         suspicionPoints.remove(uuid);

//     }

//     public void reachControl(EntityDamageByEntityEvent event) {
//         Entity damager = event.getDamager();
//         Entity gotHit = event.getEntity();


//         if (damager instanceof Player p) {
//             UUID pUUID = p.getUniqueId();

//             if(p.getGameMode() == org.bukkit.GameMode.CREATIVE) return;

//             int currentPoints = suspicionPoints.getOrDefault(pUUID, 0);

//             Location victimLoc = gotHit.getLocation();
//             Location damagerLoc = p.getEyeLocation();

//             double totalDistance = damagerLoc.distance(victimLoc);


//             int pointsToAdd = 0;

//             if (totalDistance > 3 && totalDistance <= 4) {
//                 pointsToAdd = points[0];
//                 System.out.println("Punti aggiunti(REACH)");
//             } else if (totalDistance > 4 && totalDistance <= 5) {
//                 pointsToAdd = points[1];
//                 System.out.println("Punti aggiunti(REACH)");
//             } else if (totalDistance > 5 && totalDistance <= 6) {
//                 pointsToAdd = points[2];
//                 System.out.println("Punti aggiunti(REACH)");
//             } else if (totalDistance > 6) {
//                 pointsToAdd = points[3];
//                 System.out.println("Punti aggiunti(REACH)");
//             }

//             if (pointsToAdd > 0) {
//                 suspicionPoints.put(pUUID, currentPoints + pointsToAdd);
//             }
//         }
//     }

//     public void jesusControl(PlayerMoveEvent e) {
//         Player p = e.getPlayer();

//         if (p.isFlying() || p.isInsideVehicle() || p.isSwimming()) return;

//         Material block = p.getLocation().getBlock().getType();
//         Material below = p.getLocation().subtract(0, 0.1, 0).getBlock().getType();

//         if (block == Material.WATER || below == Material.WATER) {

//             double yDiff = e.getTo().getY() - e.getFrom().getY();

//             if (Math.abs(yDiff) < 0.0001) {
//                 UUID pUUID = p.getUniqueId();
//                 int currentPoints = suspicionPoints.getOrDefault(pUUID, 0);
//                 suspicionPoints.put(pUUID, currentPoints + points[0]);
//                 System.out.println("Punti aggiunti(JESUS)");
//             }
//         }
//     }

//     public void flyControl(PlayerMoveEvent e) {

//         Player p = e.getPlayer();
//         UUID pUUID = p.getUniqueId();

//         if (p.getAllowFlight() || p.isInsideVehicle() || p.isGliding() || p.isJumping()) return;

//         Material feet = p.getLocation().getBlock().getType();
//         Material below = p.getLocation().subtract(0, 1, 0).getBlock().getType();
//         double yPos = p.getY();
//         double minY = ApexStaff.getPlugin().getConfig().getDouble("prediction.flight.min-y");
//         int flightPoints = ApexStaff.getPlugin().getConfig().getInt("prediction.flight.flight-points");


//         if(feet == Material.AIR || below == Material.AIR) {

//             double yTo = e.getTo().getY();
//             double yFrom = e.getFrom().getY();

//             if(yTo < yFrom) return;

//             double yDiff = yTo - yFrom;

//             int currentBuffer = flyBuffer.getOrDefault(pUUID, 0);
//             int newBufferValue = currentBuffer + 1;
//             flyBuffer.put(pUUID, newBufferValue);

//             if(yPos >= minY) {

//                 if(currentBuffer >= flightPoints) {

//                     int currentPoints = suspicionPoints.getOrDefault(pUUID, 0);
//                     suspicionPoints.put(pUUID, currentPoints + points[0]);
//                     System.out.println("Punti aggiunti(FLY)");

//                 }

//             }

//         } else {

//             flyBuffer.put(pUUID, 0);

//         }

//     }

//     public void speedControl(PlayerMoveEvent e) {
//         Player p = e.getPlayer();
//         UUID pUUID = p.getUniqueId();


//         if (p.hasPotionEffect(PotionEffectType.SPEED)) return;
//         if (p.isInsideVehicle()) return;

//         double dX = e.getTo().getX() - e.getFrom().getX();
//         double dZ = e.getTo().getZ() - e.getFrom().getZ();
//         double metersPerSecond = Math.sqrt(dX * dX + dZ * dZ) * 20;


//         double minSpeedVal = ApexStaff.getPlugin().getConfig().getDouble("prediction.speed.min-speed-val");
//         double currentLimit = minSpeedVal;

//         ItemStack boots = p.getInventory().getBoots();
//         if (boots != null && boots.containsEnchantment(Enchantment.SOUL_SPEED)) {
//             currentLimit = 9.5;
//         }


//         if (metersPerSecond > currentLimit) {
//             int newBuffer = speedBuffer.getOrDefault(pUUID, 0) + 1;
//             speedBuffer.put(pUUID, newBuffer);

//             if (newBuffer >= 5) {
//                 int currentPoints = suspicionPoints.getOrDefault(pUUID, 0);
//                 suspicionPoints.put(pUUID, currentPoints + points[0]);
//                 System.out.println("Punti aggiunti(SPEED)");

//                 speedBuffer.put(pUUID, 0);
//             }
//         } else {

//             speedBuffer.put(pUUID, 0);
//         }
//     }




}
