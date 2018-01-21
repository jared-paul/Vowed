package net.vowed.items;

import net.vowed.api.items.Tier;
import net.vowed.api.plugin.Vowed;
import net.vowed.core.items.config.AbstractStatStorage;
import net.vowed.core.items.config.ChanceStatStorage;
import net.vowed.core.items.config.TripleStatStorage;
import net.vowed.core.items.util.weapon.WeaponStat;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by JPaul on 6/14/2016.
 */
public class Settings
{
    FileConfiguration config;

    public Settings() throws IOException, InvalidConfigurationException
    {
        config = YamlConfiguration.loadConfiguration(new File(Vowed.getPlugin().getDataFolder() + "\\Items\\config.yml"));

        config.options().copyDefaults(true);

        for (WeaponSetting weaponSetting : WeaponSetting.values())
        {
            if (weaponSetting.hasKey(config))
            {
                String path = weaponSetting.path;

                weaponSetting.value.refreshValues(config, path);
            }
            else
            {
                config.addDefault(weaponSetting.path + ".MIN", weaponSetting.value.statMIN);

                if (weaponSetting.value instanceof TripleStatStorage)
                {
                    config.addDefault(weaponSetting.path + ".MIN-MAX", ((TripleStatStorage) weaponSetting.value).statLowHigh);
                }

                config.addDefault(weaponSetting.path + ".MAX", weaponSetting.value.statMAX);
            }
        }

        config.save(new File(Vowed.getPlugin().getDataFolder() + "\\Items\\config.yml"));
    }

    public enum ShieldSetting
    {
        TIER1_
    }

    public enum WeaponSetting
    {
        //sword
        TIER1_SWORD_COMMON_DAMAGE("items.weapon.damages.tier1.sword.common", new TripleStatStorage(1, 3, 7)),
        TIER1_SWORD_UNCOMMON_DAMAGE("items.weapon.damages.tier1.sword.uncommon", new TripleStatStorage(8, 10, 14)),
        TIER1_SWORD_RARE_DAMAGE("items.weapon.damages.tier1.sword.rare", new TripleStatStorage(15, 17, 21)),
        TIER1_SWORD_LEGENDARY_DAMAGE("items.weapon.damages.tier1.sword.legendary", new TripleStatStorage(22, 24, 28)),

        TIER2_SWORD_COMMON_DAMAGE("items.weapon.damages.tier2.sword.common", new TripleStatStorage(30, 34, 40)),
        TIER2_SWORD_UNCOMMON_DAMAGE("items.weapon.damages.tier2.sword.uncommon", new TripleStatStorage(41, 45, 51)),
        TIER2_SWORD_RARE_DAMAGE("items.weapon.damages.tier2.sword.rare", new TripleStatStorage(52, 56, 62)),
        TIER2_SWORD_LEGENDARY_DAMAGE("items.weapon.damages.tier2.sword.legendary", new TripleStatStorage(63, 67, 73)),

        TIER3_SWORD_COMMON_DAMAGE("items.weapon.damages.tier3.sword.common", new TripleStatStorage(90, 98, 110)),
        TIER3_SWORD_UNCOMMON_DAMAGE("items.weapon.damages.tier3.sword.uncommon", new TripleStatStorage(111, 119, 131)),
        TIER3_SWORD_RARE_DAMAGE("items.weapon.damages.tier3.sword.rare", new TripleStatStorage(132, 140, 152)),
        TIER3_SWORD_LEGENDARY_DAMAGE("items.weapon.damages.tier3.sword.legendary", new TripleStatStorage(153, 161, 173)),

        TIER4_SWORD_COMMON_DAMAGE("items.weapon.damages.tier4.sword.common", new TripleStatStorage(210, 222, 242)),
        TIER4_SWORD_UNCOMMON_DAMAGE("items.weapon.damages.tier4.sword.uncommon", new TripleStatStorage(243, 255, 275)),
        TIER4_SWORD_RARE_DAMAGE("items.weapon.damages.tier4.sword.rare", new TripleStatStorage(276, 288, 308)),
        TIER4_SWORD_LEGENDARY_DAMAGE("items.weapon.damages.tier4.sword.legendary", new TripleStatStorage(309, 321, 341)),

        TIER5_SWORD_COMMON_DAMAGE("items.weapon.damages.tier5.sword.common", new TripleStatStorage(400, 416, 456)),
        TIER5_SWORD_UNCOMMON_DAMAGE("items.weapon.damages.tier5.sword.uncommon", new TripleStatStorage(457, 473, 513)),
        TIER5_SWORD_RARE_DAMAGE("items.weapon.damages.tier5.sword.rare", new TripleStatStorage(514, 530, 570)),
        TIER5_SWORD_LEGENDARY_DAMAGE("items.weapon.damages.tier5.sword.legendary", new TripleStatStorage(571, 587, 627)),
        //end sword

        //axe
        TIER1_AXE_COMMON_DAMAGE("items.weapon.damages.tier1.axe.common", new TripleStatStorage(1, 4, 10)),
        TIER1_AXE_UNCOMMON_DAMAGE("items.weapon.damages.tier1.axe.uncommon", new TripleStatStorage(11, 14, 20)),
        TIER1_AXE_RARE_DAMAGE("items.weapon.damages.tier1.axe.rare", new TripleStatStorage(21, 24, 30)),
        TIER1_AXE_LEGENDARY_DAMAGE("items.weapon.damages.tier1.axe.legendary", new TripleStatStorage(31, 34, 40)),

        TIER2_AXE_COMMON_DAMAGE("items.weapon.damages.tier2.axe.common", new TripleStatStorage(40, 46, 56)),
        TIER2_AXE_UNCOMMON_DAMAGE("items.weapon.damages.tier2.axe.uncommon", new TripleStatStorage(57, 63, 73)),
        TIER2_AXE_RARE_DAMAGE("items.weapon.damages.tier2.axe.rare", new TripleStatStorage(74, 80, 90)),
        TIER2_AXE_LEGENDARY_DAMAGE("items.weapon.damages.tier2.axe.legendary", new TripleStatStorage(91, 97, 107)),

        TIER3_AXE_COMMON_DAMAGE("items.weapon.damages.tier3.axe.common", new TripleStatStorage(115, 124, 144)),
        TIER3_AXE_UNCOMMON_DAMAGE("items.weapon.damages.tier3.axe.uncommon", new TripleStatStorage(145, 154, 174)),
        TIER3_AXE_RARE_DAMAGE("items.weapon.damages.tier3.axe.rare", new TripleStatStorage(175, 184, 204)),
        TIER3_AXE_LEGENDARY_DAMAGE("items.weapon.damages.tier3.axe.legendary", new TripleStatStorage(205, 214, 234)),

        TIER4_AXE_COMMON_DAMAGE("items.weapon.damages.tier4.axe.common", new TripleStatStorage(240, 252, 292)),
        TIER4_AXE_UNCOMMON_DAMAGE("items.weapon.damages.tier4.axe.uncommon", new TripleStatStorage(293, 305, 345)),
        TIER4_AXE_RARE_DAMAGE("items.weapon.damages.tier4.axe.rare", new TripleStatStorage(346, 358, 398)),
        TIER4_AXE_LEGENDARY_DAMAGE("items.weapon.damages.tier4.axe.legendary", new TripleStatStorage(399, 411, 451)),

        TIER5_AXE_COMMON_DAMAGE("items.weapon.damages.tier5.axe.common", new TripleStatStorage(470, 486, 536)),
        TIER5_AXE_UNCOMMON_DAMAGE("items.weapon.damages.tier5.axe.uncommon", new TripleStatStorage(537, 553, 603)),
        TIER5_AXE_RARE_DAMAGE("items.weapon.damages.tier5.axe.rare", new TripleStatStorage(604, 620, 670)),
        TIER5_AXE_LEGENDARY_DAMAGE("items.weapon.damages.tier5.axe.legendary", new TripleStatStorage(671, 687, 737)),
        //end axe

        //staff
        TIER1_STAFF_COMMON_DAMAGE("items.weapon.damages.tier1.staff.common", new TripleStatStorage(1, 2, 5)),
        TIER1_STAFF_UNCOMMON_DAMAGE("items.weapon.damages.tier1.staff.uncommon", new TripleStatStorage(6, 7, 10)),
        TIER1_STAFF_RARE_DAMAGE("items.weapon.damages.tier1.staff.rare", new TripleStatStorage(11, 12, 15)),
        TIER1_STAFF_LEGENDARY_DAMAGE("items.weapon.damages.tier1.staff.legendary", new TripleStatStorage(16, 17, 20)),

        TIER2_STAFF_COMMON_DAMAGE("items.weapon.damages.tier2.staff.common", new TripleStatStorage(25, 27, 33)),
        TIER2_STAFF_UNCOMMON_DAMAGE("items.weapon.damages.tier2.staff.uncommon", new TripleStatStorage(34, 36, 42)),
        TIER2_STAFF_RARE_DAMAGE("items.weapon.damages.tier2.staff.rare", new TripleStatStorage(43, 45, 51)),
        TIER2_STAFF_LEGENDARY_DAMAGE("items.weapon.damages.tier2.staff.legendary", new TripleStatStorage(52, 54, 60)),

        TIER3_STAFF_COMMON_DAMAGE("items.weapon.damages.tier3.staff.common", new TripleStatStorage(80, 84, 96)),
        TIER3_STAFF_UNCOMMON_DAMAGE("items.weapon.damages.tier3.staff.uncommon", new TripleStatStorage(97, 99, 105)),
        TIER3_STAFF_RARE_DAMAGE("items.weapon.damages.tier3.staff.rare", new TripleStatStorage(106, 108, 114)),
        TIER3_STAFF_LEGENDARY_DAMAGE("items.weapon.damages.tier3.staff.legendary", new TripleStatStorage(115, 117, 123)),

        TIER4_STAFF_COMMON_DAMAGE("items.weapon.damages.tier4.staff.common", new TripleStatStorage(140, 148, 172)),
        TIER4_STAFF_UNCOMMON_DAMAGE("items.weapon.damages.tier4.staff.uncommon", new TripleStatStorage(173, 181, 205)),
        TIER4_STAFF_RARE_DAMAGE("items.weapon.damages.tier4.staff.rare", new TripleStatStorage(206, 214, 238)),
        TIER4_STAFF_LEGENDARY_DAMAGE("items.weapon.damages.tier4.staff.legendary", new TripleStatStorage(239, 247, 271)),

        TIER5_STAFF_COMMON_DAMAGE("items.weapon.damages.tier5.staff.common", new TripleStatStorage(300, 316, 346)),
        TIER5_STAFF_UNCOMMON_DAMAGE("items.weapon.damages.tier5.staff.uncommon", new TripleStatStorage(347, 363, 393)),
        TIER5_STAFF_RARE_DAMAGE("items.weapon.damages.tier5.staff.rare", new TripleStatStorage(394, 410, 440)),
        TIER5_STAFF_LEGENDARY_DAMAGE("items.weapon.damages.tier5.staff.legendary", new TripleStatStorage(441, 457, 487)),
        //end staff

        //bow
        TIER1_BOW_COMMON_DAMAGE("items.weapon.damages.tier1.bow.common", new TripleStatStorage(1, 8, 17)),
        TIER1_BOW_UNCOMMON_DAMAGE("items.weapon.damages.tier1.bow.uncommon", new TripleStatStorage(18, 25, 34)),
        TIER1_BOW_RARE_DAMAGE("items.weapon.damages.tier1.bow.rare", new TripleStatStorage(35, 42, 51)),
        TIER1_BOW_LEGENDARY_DAMAGE("items.weapon.damages.tier1.bow.legendary", new TripleStatStorage(52, 59, 68)),

        TIER2_BOW_COMMON_DAMAGE("items.weapon.damages.tier2.bow.common", new TripleStatStorage(78, 92, 108)),
        TIER2_BOW_UNCOMMON_DAMAGE("items.weapon.damages.tier2.bow.uncommon", new TripleStatStorage(109, 123, 138)),
        TIER2_BOW_RARE_DAMAGE("items.weapon.damages.tier2.bow.rare", new TripleStatStorage(140, 154, 170)),
        TIER2_BOW_LEGENDARY_DAMAGE("items.weapon.damages.tier2.bow.legendary", new TripleStatStorage(171, 185, 201)),

        TIER3_BOW_COMMON_DAMAGE("items.weapon.damages.tier3.bow.common", new TripleStatStorage(220, 240, 268)),
        TIER3_BOW_UNCOMMON_DAMAGE("items.weapon.damages.tier3.bow.uncommon", new TripleStatStorage(269, 289, 317)),
        TIER3_BOW_RARE_DAMAGE("items.weapon.damages.tier3.bow.rare", new TripleStatStorage(318, 338, 366)),
        TIER3_BOW_LEGENDARY_DAMAGE("items.weapon.damages.tier3.bow.legendary", new TripleStatStorage(367, 387, 415)),

        TIER4_BOW_COMMON_DAMAGE("items.weapon.damages.tier4.bow.common", new TripleStatStorage(440, 468, 516)),
        TIER4_BOW_UNCOMMON_DAMAGE("items.weapon.damages.tier4.bow.uncommon", new TripleStatStorage(517, 545, 593)),
        TIER4_BOW_RARE_DAMAGE("items.weapon.damages.tier4.bow.rare", new TripleStatStorage(594, 622, 670)),
        TIER4_BOW_LEGENDARY_DAMAGE("items.weapon.damages.tier4.bow.legendary", new TripleStatStorage(671, 699, 747)),

        TIER5_BOW_COMMON_DAMAGE("items.weapon.damages.tier5.bow.common", new TripleStatStorage(790, 826, 922)),
        TIER5_BOW_UNCOMMON_DAMAGE("items.weapon.damages.tier5.bow.uncommon", new TripleStatStorage(923, 959, 1055)),
        TIER5_BOW_RARE_DAMAGE("items.weapon.damages.tier5.bow.rare", new TripleStatStorage(1056, 1092, 1188)),
        TIER5_BOW_LEGENDARY_DAMAGE("items.weapon.damages.tier5.bow.legendary", new TripleStatStorage(1189, 1225, 1321)),
        //end bow

        //slow
        TIER1_SLOW("items.weapon.slow.tier1", new ChanceStatStorage(1, 3, 3)),
        TIER2_SLOW("items.weapon.slow.tier2", new ChanceStatStorage(1, 3, 3)),
        TIER3_SLOW("items.weapon.slow.tier3", new ChanceStatStorage(1, 3, 3)),
        TIER4_SLOW("items.weapon.slow.tier4", new ChanceStatStorage(1, 3, 3)),
        TIER5_SLOW("items.weapon.slow.tier5", new ChanceStatStorage(1, 3, 3)),

        TIER1_BLIND("items.weapon.blind.tier1", new ChanceStatStorage(1, 3, 3)),
        TIER2_BLIND("items.weapon.blind.tier2", new ChanceStatStorage(1, 3, 3)),
        TIER3_BLIND("items.weapon.blind.tier3", new ChanceStatStorage(1, 3, 3)),
        TIER4_BLIND("items.weapon.blind.tier4", new ChanceStatStorage(1, 3, 3)),
        TIER5_BLIND("items.weapon.blind.tier5", new ChanceStatStorage(1, 3, 3));

        public String path;
        public AbstractStatStorage value;

        WeaponSetting(String path, AbstractStatStorage defaultValue)
        {
            this.path = path;
            this.value = defaultValue;
        }

        public ChanceStatStorage asChanceStorage()
        {
            return (ChanceStatStorage) value;
        }

        public TripleStatStorage asTripleStorage()
        {
            return (TripleStatStorage) value;
        }

        public static WeaponSetting fromTier(Tier tier, WeaponStat stat)
        {
            for (WeaponSetting weaponSetting : values())
            {
                if (weaponSetting.name().contains(tier.name()))
                {
                    if (weaponSetting.name().contains(stat.name()))
                    {
                        return weaponSetting;
                    }
                }
            }

            return null;
        }

        public void setValue(AbstractStatStorage value)
        {
            this.value = value;
        }

        public boolean hasKey(FileConfiguration config)
        {
            return config.isSet(path);
        }
    }
}
