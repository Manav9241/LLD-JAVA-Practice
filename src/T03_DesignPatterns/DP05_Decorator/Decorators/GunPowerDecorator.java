package T03_DesignPatterns.DP05_Decorator.Decorators;

import T03_DesignPatterns.DP05_Decorator.GameCharacter;

public class GunPowerDecorator extends CharacterDecorator{
    public GunPowerDecorator(GameCharacter gc) {
        super(gc);
    }

    @Override
    public String getAbilities() {
        return (this.decoratedCharacter.getAbilities() + " with Gun Power");
    }
}
