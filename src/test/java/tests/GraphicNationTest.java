package tests;

import org.junit.jupiter.api.*;
import code.eutrustservicesapplication.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GraphicNationTest {
    @BeforeAll
    static void init()
    {
        //Platform.startup(() -> {});
        try{
            Api.start();
        }
        catch (Exception e)
        {
            fail();
        }
        try{
            ImagesNations.initialize(Api.countries());
        }
        catch (IllegalParameters e)
        {
            fail();
        }
    }
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    public void initializeOneRight() throws IOException
    {
        try
        {
            GraphicNation gN=new GraphicNation("Italy","IT");
            assertEquals("IT Italy", gN.toString());
        }
        catch (IllegalParameters e)
        {
            fail();
        }
    }
    @Test
    public void initializeOneWrong() throws IOException
    {
        try
        {
            new GraphicNation("UnitedStates","USA");
            fail();
        }
        catch (IllegalParameters e)
        {
            assertTrue(true);
        }
    }
    @Test
    public void initializeOneNull() throws IOException
    {
        try
        {
            new GraphicNation(null,null);
            fail();
        }
        catch (IllegalParameters e)
        {
            assertTrue(true);
        }
    }
    @Test
    void selection() throws IOException,IllegalParameters
    {
        Nations.initialize(Api.name_countries(),Api.countries());
        GraphicNation graphicNation=new GraphicNation();
        try
        {
            graphicNation=new GraphicNation("Austria","AT");
        }
        catch (IllegalParameters e)
        {
            fail();
        }
        graphicNation.selectNation();
        assertEquals(graphicNation.toString().substring(0,2), Nations.selected_nations.elementAt(0));
        graphicNation.selectNation();
        assertFalse(Nations.selected_nations.contains("AT"));
    }
    @Test
    void deselectionAll() throws IOException,IllegalParameters
    {
        Nations.initialize(Api.name_countries(),Api.countries());
        GraphicNation.total_selection();
        GraphicNation.reset_total_selection();
        assertEquals(0, Nations.selected_nations.size());
    }
    @Test
    void selectionAll() throws IOException,IllegalParameters
    {
        Nations.initialize(Api.name_countries(),Api.countries());
        GraphicNation.total_selection();
        assertEquals(Nations.selected_nations.size(), Api.countries().size());
    }
    @Test
    void paneTest() throws IOException,IllegalParameters
    {
        GraphicNation gN=new GraphicNation("Italy","IT");
        assertNotNull(gN.pane());
    }
    @Test
    void notNull() throws IOException,IllegalParameters
    {
        GraphicNation gN=new GraphicNation("Italy","IT");
        assertNotNull(gN);
    }

    @AfterAll
    static void stop()
    {
        //Platform.exit();
    }
}