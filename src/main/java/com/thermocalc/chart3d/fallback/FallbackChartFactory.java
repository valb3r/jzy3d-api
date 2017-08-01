package com.thermocalc.chart3d.fallback;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.image.ImageView;

import org.apache.log4j.Logger;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.keyboard.screenshot.AWTScreenshotKeyController;
import org.jzy3d.chart.controllers.keyboard.screenshot.IScreenshotKeyController;
import org.jzy3d.chart.controllers.keyboard.screenshot.IScreenshotKeyController.IScreenshotEventListener;
import org.jzy3d.chart.controllers.keyboard.screenshot.NewtScreenshotKeyController;
import org.jzy3d.chart.controllers.mouse.AWTMouseUtilities;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.chart.controllers.mouse.camera.ICameraMouseController;
import org.jzy3d.chart.controllers.mouse.camera.NewtCameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.rendering.canvas.OffscreenCanvas;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.AWTImageRenderer3d;
import org.jzy3d.plot3d.rendering.view.AWTImageRenderer3d.DisplayListener;
import org.jzy3d.plot3d.rendering.view.AWTRenderer3d;
import org.jzy3d.plot3d.rendering.view.Renderer3d;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.ui.views.ImagePanel;

public class FallbackChartFactory extends AWTChartComponentFactory {
    static Logger LOGGER = Logger.getLogger(FallbackChartFactory.class);

    public static Chart chart(Quality quality, String toolkit) {
        FallbackChartFactory f = new FallbackChartFactory();
        return f.newChart(quality, toolkit);
    }

    @Override
    public Chart newChart(IChartComponentFactory factory, Quality quality, String toolkit) {
        return new FallbackChart(factory, quality, toolkit);
    }

    public BufferedImage getScreenshotAsBufferedImage(AWTChart chart) {
        chart.screenshot();
        AWTRenderer3d renderer = (AWTRenderer3d) chart.getCanvas().getRenderer();
        BufferedImage i = renderer.getLastScreenshotImage();
        return i;
    }

    /* ########################################### */

    /**
     * Register for renderer notifications with a new JavaFX Image
     */
    public static void bind(final ImagePanel imageView, AWTChart chart) {
        if (!(chart.getCanvas().getRenderer() instanceof AWTImageRenderer3d)) {
            LOGGER.error("NOT BINDING IMAGE VIEW TO CHART AS NOT A JAVAFX RENDERER");
            return;
        }

        // Set listener on renderer to update imageView
        AWTImageRenderer3d renderer = (AWTImageRenderer3d) chart.getCanvas().getRenderer();
        renderer.addDisplayListener(new DisplayListener() {
            @Override
            public void onDisplay(Object image) {
                if (image != null) {
                    imageView.setImage((java.awt.Image) image);
                    System.out.println("image");
                    
                 // Obligatoire pour que l'image soit rafraichie.
                    // faudrait bouger ça dans le bind!!
                    ((FallbackChart) chart).getImagePanel().repaint();
                } else {
                    LOGGER.error("image is null while listening to renderer");
                }
            }
        });
        
        //imageView.setFocusable(true);
        //imageView.setF

        // imageView.setImage((java.awt.Image)renderer.getLastScreenshotImage());
    }

    public static void addPanelSizeChangedListener(ImagePanel panel, Chart chart) {
        panel.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component c = e.getComponent();

                resetTo(chart, c);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    protected static void resetTo(Chart chart, Component c) {
        int height = c.getHeight();
        int width = c.getWidth();
        resetTo(chart, width, height);
    }

    protected static void resetTo(Chart chart, double width, double height) {
        if (chart.getCanvas() instanceof OffscreenCanvas) {
            OffscreenCanvas canvas = (OffscreenCanvas) chart.getCanvas();

            // System.out.println("will init");
            canvas.initBuffer(canvas.getCapabilities(), (int) width, (int) height);
            // LOGGER.error("done initBuffer");
            chart.render();
            // LOGGER.info("done render");
        } else {
            LOGGER.error("NOT AN OFFSCREEN CANVAS!");
        }
    }

    /* ################################################# */

    @Override
    public Renderer3d newRenderer(View view, boolean traceGL, boolean debugGL) {
        return new AWTImageRenderer3d(view, traceGL, debugGL);
    }

    /* ################################################# */

    @Override
    public ICameraMouseController newMouseCameraController(Chart chart) {
        if (chart.getWindowingToolkit().equals("offscreen"))
            return new AWTCameraMouseController(chart) {
                @Override
                public void register(Chart chart) {
                    // super.register(chart);
                    if (targets == null)
                        targets = new ArrayList<Chart>(1);
                    targets.add(chart);

                    // TODO : CREATE FallbackCanvas wrapping/extending ImagePanel, rather than injecting in FallbackChart
                    //chart.getCanvas().addMouseController(this);

                    ImagePanel panel = ((FallbackChart) chart).getImagePanel();
                    panel.addMouseListener(this);
                   panel.addMouseMotionListener(this);
                   panel.addMouseWheelListener(this);
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    System.out.println("mouse press");
                    chart.render(); // CE MODE DEVRAIT POUVOIR ETRE ACTIVE DANS LA CLASSE MERE
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    System.out.println("mouse drag");
                    
                    chart.render(); // CE MODE DEVRAIT POUVOIR ETRE ACTIVE DANS LA CLASSE MERE
                    
                    
                    
                }
            };
        else
            throw new IllegalArgumentException("Unxpected chart type");
            //return new NewtCameraMouseController(chart);
    }

    /*
     * @Override public ICameraMouseController newMouseCameraController(Chart
     * chart) { ICameraMouseController mouse = new
     * JavaFXCameraMouseController(chart, null); return mouse; }
     * 
     * @Override public IMousePickingController newMousePickingController(Chart
     * chart, int clickWidth) { IMousePickingController mouse = new
     * JavaFXMousePickingController(chart, clickWidth); return mouse; }
     * 
     * @Override public ICameraKeyController newKeyboardCameraController(Chart
     * chart) { ICameraKeyController key = new JavaFXCameraKeyController(chart,
     * null); return key; }
     */

    /** TODO : replace by a JavaFXScreenshotKeyController */
    @Override
    public IScreenshotKeyController newKeyboardScreenshotController(Chart chart) {
        // trigger screenshot on 's' letter
        String file = SCREENSHOT_FOLDER + "capture-" + Utils.dat2str(new Date(), "yyyy-MM-dd-HH-mm-ss") + ".png";
        IScreenshotKeyController screenshot;

        if (!chart.getWindowingToolkit().equals("newt"))
            screenshot = new AWTScreenshotKeyController(chart, file);
        else
            screenshot = new NewtScreenshotKeyController(chart, file);

        screenshot.addListener(new IScreenshotEventListener() {
            @Override
            public void failedScreenshot(String file, Exception e) {
                System.out.println("Failed to save screenshot:");
                e.printStackTrace();
            }

            @Override
            public void doneScreenshot(String file) {
                System.out.println("Screenshot: " + file);
            }
        });
        return screenshot;
    }

    public static String SCREENSHOT_FOLDER = "./data/screenshots/";

}
