package src.Model;
/**
 * This program creates a hero character.
 *
 * Code from class.
 *
 * @author Anastasia Vilenius
 * @version 05/09/23
 */
public class HeroOne extends Hero {

    /**
     * Indicates minimum of damage that can be generated from a special attack.
     */
    private int myMinSpecialDamage;

    /**
     * Indicates maximum of damage that can be generated from a special attack.
     */
    private int myMaxSpecialDamage;

    /**
     * Constructor that initializes fields.
     *
     * @param theName the name chosen by the player for the hero
     */
    public HeroOne(final String theName) {
        super(theName, 150, 40, 60, .8, .2, 2, .2);
    }

    /**
     * Overrides specialAttack method in Adventurer class by allowing heroOne a chance
     * to use a crushing blow special attack.
     *
     * @param theOpponent the monster that the hero is battling
     */
    @Override
    protected void specialAttack(final Adventurer theOpponent) {
        System.out.print(getName() + " tries to use Crushing Blow");
        if(canUseSpecialSkill()) {
            int damage = generateSpecialDamage();

            System.out.println(" and hits for **" + damage + "** damage!!");
        }
        else {
            System.out.println(" but it fails :-(");
        }
    }

    /**
     * Generates range of values of special attack damage.
     *
     * @return range of values of special attack damage
     */
    public int generateSpecialDamage() {
        return Adventurer.generateRangedValue(myMinSpecialDamage, myMaxSpecialDamage);
    }
}
