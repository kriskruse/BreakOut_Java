package dk.group12.breakout.BreakOutGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dk.group12.breakout.BreakOutGame.GameState.*;

public class PowerUpHandler {
    public List<PowerUp> fallingPowerUps = new ArrayList<>();
    private final Map<GameState.powerUpType, PowerUp> activePowerUps = new HashMap<>();

    public PowerUpHandler(GameState.BlockCluster blockCluster) {
        assignPowerUps(blockCluster);
    }

    private void assignPowerUps(GameState.BlockCluster blockCluster) {
        int powerUpCount = 64; // Amount of power-ups per block cluster

        // Get all possible power-up types from the enum
        GameState.powerUpType[] powerUpTypes = GameState.powerUpType.values();

        while (powerUpCount > 0) {
            int randomRow = (int) (Math.random() * blockCluster.cluster.length);
            int randomCol = (int) (Math.random() * blockCluster.cluster[0].length);

            GameState.Block block = blockCluster.cluster[randomRow][randomCol];
            if (block.powerUp == GameState.powerUpType.NONE) {
                int randomIndex = (int) (Math.random() * (powerUpTypes.length - 1)) + 1; // Skip index 0 (type NONE)
                block.powerUp = powerUpTypes[randomIndex];
                powerUpCount--;
            }
        }
    }

    public static void applyPowerUpEffect(PowerUp powerUp) {
        if (powerUp.type == GameState.powerUpType.WIDEN_PLATFORM) {
            platform.x -= platform.width / 4; // Platform stays centered around same point
            platform.width *= 1.5;
        }
        if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
            for (GameState.Ball ball : ballList) {
                ball.x -= ball.radius;
                ball.y -= ball.radius;
                ball.radius *= 2;
            }
        }
        if (powerUp.type == GameState.powerUpType.MULTIBALL) {
            spawnAdditionalBall();
        }
        powerUp.activate();
    }

    // Reversing effects
    public static void removePowerUpEffect(PowerUp powerUp) {
        if (powerUp.type == GameState.powerUpType.WIDEN_PLATFORM) {
            platform.width /= 1.5;
            platform.x += platform.width / 4;
        }
        if (powerUp.type == GameState.powerUpType.ENLARGE_BALL) {
            for (GameState.Ball ball : ballList) {
                ball.radius /= 2;
                ball.x += ball.radius;
                ball.y += ball.radius;
            }
        }
    }

    public void checkForCatch(int gameHeight) {
        for (PowerUp element : fallingPowerUps) {
            if (element.y > gameHeight) {
                fallingPowerUps.remove(element);
                break;
            } else if (element.isPickedUp) {
                if (element.isStackable) {
                    applyPowerUpEffect(element);
                    fallingPowerUps.remove(element);
                    break;
                } else {
                    if (!activePowerUps.containsKey(element.type)) {
                        applyPowerUpEffect(element);
                        activePowerUps.put(element.type, element);
                    } else {
                        activePowerUps.get(element.type).extendDuration();
                    }
                    fallingPowerUps.remove(element);
                    break;
                }
            }
        }
    }

    public void spawnPowerUp(GameState.Block block) {
        if (block.powerUp != GameState.powerUpType.NONE) {
            fallingPowerUps.add(new PowerUp(block, block.powerUp));
        }
    }

    public void movePowerUps() {
        for (PowerUp powerUp : fallingPowerUps) {
            powerUp.move();
        }
    }

    public void handlePowerUpExpiration() {
        List<GameState.powerUpType> expiredPowerUps = new ArrayList<>();
        for (Map.Entry<GameState.powerUpType, PowerUp> entry : activePowerUps.entrySet()) {
            PowerUp powerUp = entry.getValue();
            if (powerUp.hasExpired()) {
                removePowerUpEffect(powerUp);
                expiredPowerUps.add(entry.getKey());
            }
        }
        // Remove expired power-ups from Map
        for (GameState.powerUpType type : expiredPowerUps) {
            activePowerUps.remove(type);
        }
    }


    public class PowerUp extends CollisionElement {
        public GameState.powerUpType type;
        private boolean isPickedUp = false;
        private long startTime; // Tracks when power is picked up
        private final long duration; // In milliseconds (0 for indefinite)
        private final boolean isStackable;

        public PowerUp(GameState.Block block, GameState.powerUpType type) {
            super(block.x + (block.width - 15) / 2,
                    block.y + (block.height - 15) / 2,
                    15, 15);
            this.type = type;

            // For non-timed power-ups
            if (type == GameState.powerUpType.MULTIBALL) {
                this.duration = 0;
                this.isStackable = true;
            } else {
                this.duration = 10_000;
                this.isStackable = false;
            }
        }

        public void move() {
            this.y += 5;
        }

        public void activate() {
            this.isPickedUp = true;
            this.startTime = System.currentTimeMillis(); // Record time of pickup
        }

        public boolean hasExpired() {
            if (!isPickedUp || duration == 0) { return false; }
            return (System.currentTimeMillis() - startTime) >= duration;
        }

        public void extendDuration() {
            this.startTime = System.currentTimeMillis();
        }

    }
}
