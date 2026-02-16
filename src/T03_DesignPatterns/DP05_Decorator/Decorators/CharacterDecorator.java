package T03_DesignPatterns.DP05_Decorator.Decorators;

import T03_DesignPatterns.DP05_Decorator.GameCharacter;

public abstract class CharacterDecorator implements GameCharacter {
    protected GameCharacter decoratedCharacter;

    public CharacterDecorator(GameCharacter character) {
        this.decoratedCharacter = character;
    }
}
