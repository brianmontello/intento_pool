import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class pool extends JPanel implements MouseMotionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private int paloX = WIDTH / 2 - 50;
    private int paloY = HEIGHT / 50;
    private int paloWidth = 100;
    private int paloHeight = 2;
    private int pelotaDiameter = 30;
    private int pelotaX = (WIDTH - pelotaDiameter) / 2;
    private int pelotaY = (HEIGHT - pelotaDiameter) / 2;
    private int pelotaVelX = 0;
    private int pelotaVelY = 0;
    private int pelota2Diameter = 30;
    private int pelota2X = WIDTH / 2 + 50;
    private int pelota2Y = HEIGHT / 2;
    private int pelota2VelX = 0;
    private int pelota2VelY = 0;


    public pool() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        addMouseMotionListener(this);

        // Bucle de animación
        new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moverPelota();
                repaint();
            }
        }).start();
    }

    private void moverPelota() {
        // Mover la pelota
        pelotaX += pelotaVelX;
        pelotaY += pelotaVelY;

        // Mover la segunda pelota
        pelota2X += pelota2VelX;
        pelota2Y += pelota2VelY;

     // Calcular la distancia entre el centro de la pelota y el lado del palo
        double distanciaX = Math.abs(pelotaX - (paloX + paloWidth / 2));
        double distanciaY = Math.abs(pelotaY - (paloY + paloHeight / 2));
        double distanciaAlLadoX = distanciaX - paloWidth / 2;
        double distanciaAlLadoY = distanciaY - paloHeight / 2;
        double distanciaTotal = Math.sqrt(distanciaAlLadoX * distanciaAlLadoX + distanciaAlLadoY * distanciaAlLadoY);

        // Verificar colisión entre la esquina del palo y la pelota
        if (distanciaTotal <= pelotaDiameter / 2) {
            // Hay colisión entre la esquina del palo y la pelota
            // Cambiar la velocidad de la pelota según sea necesario
            pelotaVelY = -pelotaVelY;
        }

        
        
     // Verificar colisión entre las dos pelotas
        double distanciaEntrePelotas = distancia(pelotaX, pelotaY, pelota2X, pelota2Y);
        if (distanciaEntrePelotas <= pelotaDiameter) {
            // Intercambiar velocidades para simular la colisión
            int tempVelX = pelotaVelX;
            int tempVelY = pelotaVelY;
            pelotaVelX = pelota2VelX;
            pelotaVelY = pelota2VelY;
            pelota2VelX = tempVelX;
            pelota2VelY = tempVelY;
        }

        // Rebotar la pelota en las paredes
        if (pelotaX <= 0 || pelotaX + pelotaDiameter >= WIDTH) {
            pelotaVelX = -pelotaVelX;
        }
        if (pelotaY <= 0 || pelotaY + pelotaDiameter >= HEIGHT) {
            pelotaVelY = -pelotaVelY;
        }

     // Rebotar la segunda pelota en las paredes
        if (pelota2X <= 0 || pelota2X + pelota2Diameter >= WIDTH) {
            pelota2VelX = -pelota2VelX;
        }

        // Rebotar la segunda pelota en el techo y el suelo
        if (pelota2Y <= 0 || pelota2Y + pelota2Diameter >= HEIGHT) {
            pelota2VelY = -pelota2VelY;
        }


        // Verificar colisión con el palo (rectángulo)
        Rectangle paloRect = new Rectangle(paloX, paloY, paloWidth, paloHeight);
        Ellipse2D pelotaEllipse = new Ellipse2D.Double(pelotaX, pelotaY, pelotaDiameter, pelotaDiameter);

        if (paloRect.intersects(pelotaEllipse.getBounds2D())) {
            // Hay colisión entre el palo y la pelota
            // Cambiar la velocidad de la pelota según sea necesario
            pelotaVelY = -pelotaVelY;
        }
    }
    
    private double distancia(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar pelota estática en el centro de la pantalla
        g.setColor(Color.RED);
        g.fillOval(pelotaX, pelotaY, pelotaDiameter, pelotaDiameter);

        // Dibujar segunda pelota
        g.setColor(Color.YELLOW);
        g.fillOval(pelota2X, pelota2Y, pelota2Diameter, pelota2Diameter);
        
        // Dibujar palo en la posición actual del mouse
        g.setColor(Color.BLUE);
        g.fillRect(paloX, paloY, paloWidth, paloHeight);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Actualizar la posición del palo según la posición del mouse
        paloX = e.getX() - paloWidth / 2;
        paloY = e.getY() - paloHeight / 2;

        // Asegurar que el palo no salga de los límites de la pantalla
        if (paloX < 0) {
            paloX = 0;
        } else if (paloX > WIDTH - paloWidth) {
            paloX = WIDTH - paloWidth;
        }

        if (paloY < 0) {
            paloY = 0;
        } else if (paloY > HEIGHT - paloHeight) {
            paloY = HEIGHT - paloHeight;
        }

        // Verificar colisión con la pelota cuando se mueve el mouse
        Rectangle pelotaRect = new Rectangle(pelotaX, pelotaY, pelotaDiameter, pelotaDiameter);
        Rectangle paloRect = new Rectangle(paloX, paloY, paloWidth, paloHeight);

        if (pelotaRect.intersects(paloRect)) {
            // Cambiar la velocidad de la pelota solo si el mouse está moviéndose
            pelotaVelX = (int) (Math.random() * 10) - 5; // Velocidad horizontal aleatoria entre -5 y 5
            pelotaVelY = (int) (Math.random() * 10) - 5; // Velocidad vertical aleatoria entre -5 y 5
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pool?");
        pool game = new pool();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
