package com.doo.xattr.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/**
 * Expend Attributes
 */
public interface ExAttributes {

    interface Living {

        Attribute CONSUMER_TIME = new RangedAttribute("ex.consumer_time", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        /**
         * extra xp of add
         */
        Attribute  XP = new RangedAttribute("ex.xp_add", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);


        interface Hurt {
            Attribute SPEED = new RangedAttribute("ex.hurt.speed", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute INTERVAL = new RangedAttribute("ex.hurt.interval", 0, Integer.MIN_VALUE, 10);

            Attribute REACH = new RangedAttribute("ex.hurt.reach", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            /**
             * if rate is 50 then effect on entity is 0.05
             *
             * value is 10x
             */
            Attribute CRIT_RATE = new RangedAttribute("ex.hurt.crit_range", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute CRIT_VALUE = new RangedAttribute("ex.hurt.crit_rate", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute SWEEPING_RANGE = new RangedAttribute("ex.hurt.sweeping_range", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute SWEEPING_VALUE = new RangedAttribute("ex.hurt.sweeping_rate", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute RANGE = new RangedAttribute("ex.hurt.range", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute PASS = new RangedAttribute("ex.hurt.pass", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute REAL_VALUE = new RangedAttribute("ex.hurt.real_value", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Attribute REBOUND = new RangedAttribute("ex.hurt.rebound", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

    }
}
