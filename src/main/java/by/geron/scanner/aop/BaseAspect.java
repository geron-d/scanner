package by.geron.scanner.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BaseAspect {

    public static final String ARGS = " ARGUMENTS is ";
    public static final String RESULT = " RESULT is ";
    public static final String URI = " URI is ";
    public static final String PREFIX = "#";
    public static final String PREFIX_CONTROLLER_BEFORE = "---> ";
    public static final String PREFIX_CONTROLLER_AFTER = "<--- ";

    public static final String BEFORE_PATTERN_SERVICE = PREFIX + "{}:" + ARGS + "{}";
    public static final String AFTER_PATTERN_SERVICE = PREFIX + "{}:" + RESULT + "{{}}," + ARGS + "{}";

    public static final String BEFORE_PATTERN_CONTROLLER = PREFIX_CONTROLLER_BEFORE + "{}" + "{}:" + URI + "{}," + ARGS + "{}";
    public static final String AFTER_PATTERN_CONTROLLER = PREFIX_CONTROLLER_AFTER + "{}" + "{}:" + URI + "{}, result is {}," + ARGS + "{}";

    protected String getARgsWithNames(JoinPoint point) {
        String[] parameterNames = ((CodeSignature) point.getSignature()).getParameterNames();
        Object[] args = point.getArgs();
        StringBuilder stringBuilder = new StringBuilder("{");
        for (int i = 0; i < args.length; i++) {
            stringBuilder
                    .append(parameterNames[i])
                    .append("#")
                    .append(getStringInstanceOf(Optional.ofNullable(args[i]).orElse("not defined")));
            if (i != args.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    protected String getStringInstanceOf(Object result) {
        if (result instanceof Object[]) {
            return Arrays.toString((Object[]) result);
        }
        if (result instanceof HashMap) {
            Map<Object, Object> resultMap = (Map) result;
            return resultMap.entrySet()
                    .stream()
                    .map(entry -> String.join("#", entry.getKey().toString(), entry.getValue().toString()))
                    .collect(Collectors.joining(", ", "{", "}"));
        }
        return Optional.ofNullable(result).orElse("not defined").toString();
    }

}
