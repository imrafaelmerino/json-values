package jsonvalues

import java.util.function.{BiFunction, Function, IntBinaryOperator, LongBinaryOperator, Predicate, Supplier}

object ScalaToJava
{


  def supplier[T] = (fn: () => T) => new Supplier[T]
  {
    override def get(): T = fn.apply()
  }

  def predicate[T] = (p: T => Boolean) => new Predicate[T]
  {
    override def test(t: T): Boolean = p(t)
  }

  def function[I, O] = (f: I => O) => new Function[I, O]
  {
    override def apply(t: I): O = f(t)
  }

  def bifunction[T, U, R] = (f: (T, U) => R) => new BiFunction[T, U, R]
  {
    override def apply(t: T,
                       u: U
                      ): R = f.apply(t,
                                     u
                                    )
  }



}
