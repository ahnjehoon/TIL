import entity.Blog;
import entity.Post;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, MalformedURLException {
        var post1 = new Post(1, "첫번째 글", "제곧내", false);
        var blog = new Blog("첫번째 블로그", new URL("http://a.b.c"), post1);
        System.out.println(objectToJson(blog, 0));

        var post2 = new Post(2, "두번째 글", "제곧내", false, "퍼가요~ 1", "퍼가요~ 2");
//        System.out.println(objectToJson(post2, 0));

        var post3 = new Post(3, "세번째 글", "제곧내", false, new String[]{"퍼가요~ 1", "퍼가요~ 2"});
        var blog2 = new Blog("두번째 블로그", new URL("http://a.b.c.d"), post1, post2, post3);
        System.out.println(objectToJson(blog2, 0));
    }

    private static String objectToJson(Object instance, int indentSize) throws IllegalAccessException {
        var fields = instance.getClass().getDeclaredFields();
        var sb = new StringBuilder();

        sb.append(indent(indentSize));
        sb.append("{");
        sb.append("\n");

        for (int i = 0; i < fields.length; i++) {
            var field = fields[i];
            field.setAccessible(true);

            if (field.isSynthetic()) {
                continue;
            }

            sb.append(indent(indentSize + 1));
            sb.append(formatStringValue(field.getName()));
            sb.append(":");

            if (field.getType().equals(String.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isPrimitive()) {
                sb.append(formatPrimitiveValue(field.get(instance), field.getType()));
            } else if (field.getType().equals(java.net.URL.class)) {
                sb.append(formatStringValue(field.get(instance).toString()));
            } else if (field.getType().isArray()) {
                sb.append(arrayToJson(field.get(instance), indentSize + 1));
            } else {
                sb.append(objectToJson(field.get(instance), indentSize + 1));
            }

            if (i != fields.length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("}");

        return sb.toString();
    }

    private static String arrayToJson(Object arrayInstance, int indentSize) throws IllegalAccessException {
        var sb = new StringBuilder();

        var length = Array.getLength(arrayInstance);
        var componentType = arrayInstance.getClass().getComponentType();

        sb.append("[");
        sb.append("\n");

        for (int i = 0; i < length; i++) {
            var obj = Array.get(arrayInstance, i);

            if (componentType.isPrimitive()) {
                sb.append(indent(indentSize + 1));
                sb.append(formatPrimitiveValue(obj, componentType));
            } else if (componentType.equals(String.class)) {
                sb.append(indent(indentSize + 1));
                sb.append(formatStringValue(obj.toString()));
            } else {
                sb.append(objectToJson(obj, indentSize + 1));
            }

            if (i != length - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append(indent(indentSize));
        sb.append("]");

        return sb.toString();
    }

    private static String formatStringValue(String value) {
        return String.format("\"%s\"", value);
    }

    private static String formatPrimitiveValue(Object instance, Class<?> type) {
        if (type.equals(boolean.class) || type.equals(int.class) || type.equals(long.class)) {
            return instance.toString();
        }

        throw new RuntimeException(String.format("Type : %s 은 지원되지 않습니다", type.getName()));
    }

    private static String indent(int indentSize) {
        return "\t".repeat(Math.max(0, indentSize));
    }
}
