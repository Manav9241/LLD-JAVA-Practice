package T03_DesignPatterns.DP05_Decorator.Decorators;

import T03_DesignPatterns.DP05_Decorator.GameCharacter;

public class StarPowerDecorator extends CharacterDecorator{
    public StarPowerDecorator(GameCharacter gc) {
        super(gc);
    }

    @Override
    public String getAbilities() {
        return (this.decoratedCharacter.getAbilities() + " With Star Power");
    }
}
