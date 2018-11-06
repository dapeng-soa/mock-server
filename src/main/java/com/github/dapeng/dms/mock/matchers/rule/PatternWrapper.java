package com.github.dapeng.dms.mock.matchers.rule;


import com.github.dapeng.router.pattern.*;
import lombok.Getter;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-06 10:36 AM
 */
public class PatternWrapper {
    @Getter
    public enum PatternEnum {
        NotPattern(1, com.github.dapeng.router.pattern.NotPattern.class),
        StringPattern(2, com.github.dapeng.router.pattern.StringPattern.class),
        RegexPattern(3, com.github.dapeng.router.pattern.RegexPattern.class),
        RangePattern(4, com.github.dapeng.router.pattern.RangePattern.class),
        IpPattern(5, com.github.dapeng.router.pattern.IpPattern.class),
        ModePattern(6, com.github.dapeng.router.pattern.ModePattern.class),
        NumberPattern(7, com.github.dapeng.router.pattern.NumberPattern.class);

        private int id;
        private Class<? extends Pattern> patternClass;

        PatternEnum(int id, Class<? extends Pattern> patternClass) {
            this.id = id;
            this.patternClass = patternClass;
        }
    }

    public int type;
    public Pattern pattern;

    public PatternWrapper(int type, Pattern pattern) {
        this.type = type;
        this.pattern = pattern;
    }


    public static Class<? extends Pattern> findPatternClass(int type) {
        switch (type) {
            case 1:
                return PatternEnum.NotPattern.patternClass;
            case 2:
                return PatternEnum.StringPattern.patternClass;
            case 3:
                return PatternEnum.RegexPattern.patternClass;
            case 4:
                return PatternEnum.RangePattern.patternClass;
            case 5:
                return PatternEnum.IpPattern.patternClass;
            case 6:
                return PatternEnum.ModePattern.patternClass;
            case 7:
                return PatternEnum.NumberPattern.patternClass;
            default:
                return PatternEnum.StringPattern.patternClass;
        }
    }

}
