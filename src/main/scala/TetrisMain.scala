import TetrisMain.blocSize
import Shapes.{I, J, L, S}
import indigo.shared.datatypes.Fill.Color
import indigo.shared.datatypes.Point
import indigo.*
import indigoextras.subsystems.FPSCounter

import java.io.ObjectInputFilter.Config
import scala.language.postfixOps
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.Random

@JSExportTopLevel("IndigoGame")
object TetrisMain extends IndigoSandbox[Unit, Grid] {
  private val magnification = 3

  val config: GameConfig = defaultGameConfig


  val animations: Set[Animation] =
    Set()


  val blocSize = 32

  val assets: Set[AssetType] =
    Set(
      AssetType.Audio(AssetName("Musique"), AssetPath("./assets/TetrisMusic.mp3")),
    )

  val fonts: Set[FontInfo] =
    Set()

  val shaders: Set[Shader] =
    Set()


  def setup (assetCollection : AssetCollection,dice : Dice) : Outcome[Startup[Unit]] =

    Outcome(
      Startup.Success(())
    )

  def initialModel(startupData: Unit): Outcome[Grid] = Outcome(new Grid(1024, 720))
  def boot(flags: Map[String, String]): Outcome[BootResult[Unit]] =
    Outcome(
      BootResult.noData(GameConfig.default)
        .withSubSystems(FPSCounter(Point(10)))
    )

  var lastPlay = Millis(0);
  def updateModel(context : FrameContext[Unit] , g : Grid) : GlobalEvent => Outcome[Grid] = {

    // pour déplacer la piece vers la droite
    case FrameTick if context.inputState.keyboard.keysAreDown(Key.RIGHT_ARROW) =>
      Outcome(g.playGoRight())


    // pour déplacer la piece vers la gauche
    case FrameTick if context.inputState.keyboard.keysAreDown(Key.LEFT_ARROW) =>
      Outcome(g.playGoLeft())


    // pour acceler la chute
    case FrameTick if context.inputState.keyboard.keysAreDown(Key.DOWN_ARROW) =>
      Outcome(g.playClassic())

    // pour déplacer la piece vers le bas
    case FrameTick =>
      if (lastPlay > Millis(300)) {
        lastPlay = Millis(0);

        Outcome(g.playClassic())
      }
      else {
        lastPlay = lastPlay + context.gameTime.delta.toMillis
        Outcome(g)
      }

    case _ =>{Outcome(g)}
  }

  def present(context: FrameContext[Unit], g: Grid): Outcome[SceneUpdateFragment] = {

    Outcome(
      SceneUpdateFragment.empty.withAudio(
        SceneAudio(
          SceneAudioSource(
            BindingKey("My bg music"),
            PlaybackPattern.SingleTrackLoop(Track(AssetName("Musique"), Volume(0.5)))
          )
        )
      ).addLayers(g.display())
    )
  }

}







