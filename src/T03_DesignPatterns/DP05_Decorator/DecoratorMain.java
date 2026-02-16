package T03_DesignPatterns.DP05_Decorator;

import T03_DesignPatterns.DP05_Decorator.Decorators.GunPowerDecorator;
import T03_DesignPatterns.DP05_Decorator.Decorators.HeightUpDecorator;
import T03_DesignPatterns.DP05_Decorator.Decorators.StarPowerDecorator;

public class DecoratorMain {
    public static void main(String[] args) {
        GameCharacter mario = new Mario();
        System.out.println(mario.getAbilities());

        mario = new HeightUpDecorator(mario);
        System.out.println(mario.getAbilities());

        mario = new StarPowerDecorator(mario);
        System.out.println(mario.getAbilities());

        mario = new GunPowerDecorator(mario);
        System.out.println(mario.getAbilities());
    }
}
