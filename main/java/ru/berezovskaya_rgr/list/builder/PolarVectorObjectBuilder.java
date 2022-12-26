package ru.berezovskaya_rgr.list.builder;

import java.util.Random;

import ru.berezovskaya_rgr.list.Comparator;
import ru.berezovskaya_rgr.list.PolarVector;

public class PolarVectorObjectBuilder implements UserTypeBuilder {
    @Override
    public String typeName() {
        return "PolarVector";
    }

    @Override
    public PolarVector create() {
        PolarVector vector = new PolarVector();
        vector.setAngle(new Random().nextDouble());
        vector.setLength(new Random().nextDouble());
        return vector;
    }

    @Override
    public PolarVector createFromString(String s) {
        PolarVector vector = new PolarVector();
        vector.setLength(Double.parseDouble(s.split(" ")[0]));
        vector.setAngle(Double.parseDouble(s.split(" ")[1]));
        return vector;
    }

    @Override
    public String toString(Object object) {
        return object.toString();
    }

    @Override
    public Comparator getComparator() {
        return (o1, o2) -> ((PolarVector)o1).getLength() - ((PolarVector)o2).getLength();

    }
}
