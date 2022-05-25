package Gameplay;

public interface Visitor
{
    boolean visitBlockGroup(BlockGroup o);
    boolean visitGameObj(GameObj o);
    void visitDynamicObj(DynamicObj o);
    boolean visitRing(Ring o);
}
