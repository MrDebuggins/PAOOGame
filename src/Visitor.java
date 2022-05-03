public interface Visitor
{
    public void visitBlockGroup(GameObj o);
    public void visitGameObj(GameObj o);
    public void visitDynamicObj(GameObj o);
}
