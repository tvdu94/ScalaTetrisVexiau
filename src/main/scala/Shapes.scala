import scala.util.Random

enum Shapes:
/*Ce sont les differentes pieces officieles de tetris
* Pour avoir le détail des couleurs et des formes, il faut consulter le fichier "infoShapes.txt"
* */
  case I,J,L,O,S,T,Z;


object Shapes:
  /*Permet de recuperer une piece aléatoire*/
  def getRandom: Shapes = {
    val nb = new Random().nextInt(Shapes.values.length)
    val piece = Shapes.values(nb)
    piece
  }
