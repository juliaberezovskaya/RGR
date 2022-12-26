package ru.berezovskaya_rgr.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ru.berezovskaya_rgr.list.builder.DoubleObjectBuilder;
import ru.berezovskaya_rgr.list.builder.PolarVectorObjectBuilder;
import ru.berezovskaya_rgr.list.builder.UserTypeBuilder;

public class UserFactory {
    private final static List<UserTypeBuilder> builders = new ArrayList<>();

    static {
        ArrayList<UserTypeBuilder> buildersClasses = new ArrayList<>(Arrays.asList(new DoubleObjectBuilder(), new PolarVectorObjectBuilder()));

        buildersClasses.forEach(bc -> builders.add(bc));
    }

    public static Set<String> getAllTypes() {
        return builders.stream().map(UserTypeBuilder::typeName).collect(Collectors.toSet());
    }

    public static UserTypeBuilder getBuilder(String name) {
        if (name == null) throw new NullPointerException();
        for (UserTypeBuilder b : builders) {
            if (name.equals(b.typeName())) return b;
        }
        throw new IllegalArgumentException();
    }
}
