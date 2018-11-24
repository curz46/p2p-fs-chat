package me.dylancz.chatter.gui;

import me.dylancz.chatter.event.DefaultEventBus;
import me.dylancz.chatter.event.Subscribe;
import me.dylancz.chatter.event.user.UserCreateEvent;
import me.dylancz.chatter.event.user.UserDiscardEvent;
import me.dylancz.chatter.event.user.UserMessageEvent;
import me.dylancz.chatter.user.User;

import javax.swing.*;
import javax.swing.text.Caret;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

public class Window {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private final JFrame frame;
    private JPanel messagesPanel;
    private JScrollPane messageScrollPane;


    public Window(final Consumer<String> onMessageSubmit) {
        this.frame = new JFrame();
        this.frame.setTitle("Chatter");
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final Dimension dim = new Dimension(WIDTH, HEIGHT);
        this.frame.setMinimumSize(dim);
        this.frame.setPreferredSize(dim);

        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Create two organizational JPanels

        final JPanel messagesContainer = new JPanel(new BorderLayout());
        messagesContainer.setPreferredSize(new Dimension(496, 600));
        messagesContainer.setOpaque(false);

        this.messagesPanel = new JPanel();
        this.messagesPanel.setLayout(new BoxLayout(this.messagesPanel, BoxLayout.Y_AXIS));

        this.messageScrollPane = new JScrollPane(
            this.messagesPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        this.messageScrollPane.getVerticalScrollBar()
            .addAdjustmentListener(e -> e.getAdjustable().setValue(e.getAdjustable().getMaximum()));
        this.messageScrollPane.setPreferredSize(new Dimension(500, 400));
        this.messageScrollPane.setMaximumSize(new Dimension(500, 400));
        this.messageScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        messagesContainer.add(this.messageScrollPane, BorderLayout.BEFORE_FIRST_LINE);

        final JPanel messageBoxContainer = new JPanel();
        messageBoxContainer.setPreferredSize(new Dimension(500, 200));
        messageBoxContainer.setLayout(new BorderLayout());
        messageBoxContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageBoxContainer.setOpaque(false);
        final JTextArea messageBox = new JTextArea();
        messageBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    final String content = messageBox.getText();
                    onMessageSubmit.accept(content);
                    Window.this.update(new UserMessage("You", Date.from(Instant.now()), content));
                    messageBox.setText(null);
                    e.consume();
                }
            }
        });
        messageBoxContainer.add(messageBox);
        messagesContainer.add(messageBoxContainer);

        contentPane.add(messagesContainer, BorderLayout.LINE_START);

        final JPanel onlineContainer = new JPanel();
        final JScrollPane onlineScroll = new JScrollPane(onlineContainer);
        onlineScroll.setPreferredSize(new Dimension(300, 600));
        contentPane.add(onlineScroll, BorderLayout.LINE_END);

        this.frame.getContentPane().add(contentPane);

        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void register(final DefaultEventBus eventBus) {
        eventBus.registerListeners(new MessageListener());
    }

    public void update(final Message message) {
        this.messagesPanel.add(message.createComponent());
        this.messagesPanel.revalidate();
        this.messagesPanel.repaint();
    }

    public class MessageListener {

        @Subscribe
        public void onUserCreate(final UserCreateEvent event) {
            final User user = event.getUser();
            final String id = user.getUUID().toString();
            Window.this.update(new SystemMessage(
                this.getNow(),
                id.substring(0, 8) + "... joined the conversation."
            ));
        }

        @Subscribe
        public void onUserDiscard(final UserDiscardEvent event) {
            final User user = event.getUser();
            final String id = user.getUUID().toString();
            Window.this.update(new SystemMessage(
                this.getNow(),
                id.substring(0, 8) + "... left the conversation."
            ));
        }

        @Subscribe
        public void onUserMessage(final UserMessageEvent event) {
            final User user = event.getUser();
            final String id = user.getUUID().toString();
            Window.this.update(new UserMessage(id.substring(0, 8) + "...", this.getNow(), event.getContent()));
        }

        private Date getNow() {
            return Date.from(Instant.now());
        }

    }

}
