package framework

import org.scalajs.dom.Element
import org.scalajs.dom.raw.HTMLElement
import rx._

import scala.annotation.tailrec
import scala.util.{Failure, Success}
import scalatags.JsDom.all._

/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
trait Framework {

  /**
    * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
    * to propagate changes into the DOM.
    */
  implicit def RxFrag[T](n: Rx[T])(implicit f: T => Frag, ctx: Ctx.Owner): Frag = {

    @tailrec def clearChildren(node: org.scalajs.dom.Node): Unit = {
      if(node.firstChild != null) {
        node.removeChild(node.firstChild)
        clearChildren(node)
      }
    }

    def fSafe: Frag = n match {
      case r: Rx.Dynamic[T] => r.toTry match {
        case Success(v) => v.render
        case Failure(e) => span(e.getMessage, backgroundColor := "red").render
      }
      case v: Var[T] => v.now.render
    }

    var last = fSafe.render

    val container = span(cls:="_rx")(last).render

    n.triggerLater {
      val newLast = fSafe.render
      //Rx[Seq[T]] can generate multiple children per propagate, so use clearChildren instead of replaceChild
      clearChildren(container)
      container.appendChild(newLast)
      last = newLast
    }
    bindNode(container)
  }

  implicit def RxAttrValue[T: AttrValue](implicit ctx: Ctx.Owner) = new AttrValue[Rx.Dynamic[T]]{
    def apply(t: Element, a: Attr, r: Rx.Dynamic[T]): Unit = {
      r.trigger { implicitly[AttrValue[T]].apply(t, a, r.now) }
    }
  }

  implicit def RxStyleValue[T: StyleValue](implicit ctx: Ctx.Owner) = new StyleValue[Rx.Dynamic[T]]{
    def apply(t: Element, s: Style, r: Rx.Dynamic[T]): Unit = {
      r.trigger { implicitly[StyleValue[T]].apply(t, s, r.now) }
    }
  }
}

object Framework extends Framework
