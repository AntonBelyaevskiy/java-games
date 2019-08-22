package my.home.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import my.home.GameScreen;
import my.home.Weapon;

public class Monster extends GameCharacter {
    private float moveTimer;
    private float activityRadius;

    public Monster(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.texture = new Texture("zombi.png");
        this.textureHp = new Texture("bar.png");
        this.regions = new TextureRegion(texture).split(80, 80)[0];
        this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        while (!gameScreen.getMap().isCellPassable(position)){
            this.position = new Vector2(MathUtils.random(0, 1280), MathUtils.random(0, 720));
        }

        this.direction = new Vector2(0, 0);
        this.hpMax = 20.0f;
        this.hp = hpMax;
        this.speed = 40.0f;
        this.activityRadius = 200.0f;
        this.weapon = new Weapon("Rusty sword", 50.0f, 0.8f, 5.0f);
        this.secondsPerFrame = 0.2f;
    }

    @Override
    public void update(float dt) {
        animationTimer += dt;
        damageEffectTimer -= dt;
        if (damageEffectTimer < 0.0f) {
            damageEffectTimer = 0.0f;
        }

        float dst = gameScreen.getHero().getPosition().dst(this.position);
        if (dst < activityRadius) {
            direction.set(gameScreen.getHero().getPosition()).sub(this.position).nor();
        } else {
            moveTimer -= dt;
            if (moveTimer < 0.0f) {
                moveTimer = MathUtils.random(1.0f, 5.0f);
                direction.set(MathUtils.random(-1.0f, 1.0f), MathUtils.random(-1.0f, 1.0f));
                direction.nor();
            }
        }

        moveForward(dt);

        if (dst < weapon.getAttackRadius()) {
            attackTimer += dt;
            if (attackTimer >= weapon.getAttackPeriod()) {
                attackTimer = 0.0f;
                gameScreen.getHero().takeDamage(weapon.getDamage());
            }
        }
        checkScreenBounds();
    }
}
