package framework

import org.scalajs.dom.Element
import org.scalajs.dom.raw.HTMLElement
import rx._

import scala.util.{Failure, Success}
import scalatags.JsDom.all._

/**
 * A minimal binding between Scala.Rx and Scalatags and Scala-Js-Dom
 */
trait LowPriorityFramework { self: Framework =>

  /**
    * Wraps reactive strings in spans, so they can be referenced/replaced
    * when the Rx changes.
    */
  implicit def RxStr[T](r: Rx[T])(implicit f: T => Frag, ctx: Ctx.Owner): Frag = {
    RxHtmlTag(Rx(span(r())))
  }
}

trait Framework extends LowPriorityFramework {

  def inDOM(elem: HTMLElement): Boolean = {
    if(elem == org.scalajs.dom.document.documentElement) true
    else if(elem.parentElement == null) false
    else inDOM(elem.parentElement)
  }

  /**
    * Sticks some Rx into a Scalatags fragment, which means hooking up an Obs
    * to propagate changes into the DOM.
    */
  implicit def RxHtmlTag(n: Rx[HtmlTag])(implicit ctx: Ctx.Owner): Frag = {
    def rSafe = n match {
      case r: Rx.Dynamic[HtmlTag] => r.toTry match {
        case Success(v) => v.render
        case Failure(e) => span(e.getMessage, backgroundColor := "red").render
      }
      case v: Var[HtmlTag] => v.now.render
    }
    var last = rSafe
    n.triggerLater {
      val newLast = rSafe
      println("In dom? " + inDOM(last))
      if(last.parentElement != null) {
        last.parentElement.replaceChild(newLast, last)
      } else {
        println("Was null!: " + n)
      }
      last = newLast
    }
    bindNode(last)
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
