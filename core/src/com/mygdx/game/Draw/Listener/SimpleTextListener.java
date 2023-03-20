//package com.mygdx.game.Draw.Listener;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.MathUtils;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.TextField;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
//import com.badlogic.gdx.utils.Timer;
//import com.mygdx.game.Draw.Actor.SimpleTextFieldActor;
//
//public class SimpleTextListener extends ClickListener {
//    private SimpleTextFieldActor simpleTextFieldActor;
//
//    public SimpleTextListener(SimpleTextFieldActor textFieldActor){
//        simpleTextFieldActor = textFieldActor;
//    }
//
//    public void clicked(InputEvent event, float x, float y) {
////        int count = this.getTapCount() % 4;
////        if (count == 0) {
////            TextField.this.clearSelection();
////        }
////
////        if (count == 2) {
////            int[] array = TextField.this.wordUnderCursor(x);
////            TextField.this.setSelection(array[0], array[1]);
////        }
////
////        if (count == 3) {
////            TextField.this.selectAll();
////        }
//
//    }
//
//    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//        if(pointer == 0 && button != 0 && super.touchDown(event, x, y, pointer, button)){
//            return true;
//        }
//        return false;
//
//            this.setCursorPosition(x, y);
//            TextField.this.selectionStart = TextField.this.cursor;
//            Stage stage = TextField.this.getStage();
//            if (stage != null) {
//                stage.setKeyboardFocus(TextField.this);
//            }
//
//            TextField.this.keyboard.show(true);
//            TextField.this.hasSelection = true;
//            return true;
//    }
//
//    public void touchDragged(InputEvent event, float x, float y, int pointer) {
//        super.touchDragged(event, x, y, pointer);
//        this.setCursorPosition(x, y);
//    }
//
//    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//        if (TextField.this.selectionStart == TextField.this.cursor) {
//            TextField.this.hasSelection = false;
//        }
//
//        super.touchUp(event, x, y, pointer, button);
//    }
//
//    protected void setCursorPosition(float x, float y) {
//        TextField.this.cursor = TextField.this.letterUnderCursor(x);
//        TextField.this.cursorOn = TextField.this.focused;
//        TextField.this.blinkTask.cancel();
//        if (TextField.this.focused) {
//            Timer.schedule(TextField.this.blinkTask, TextField.this.blinkTime, TextField.this.blinkTime);
//        }
//
//    }
//
//    protected void goHome(boolean jump) {
//        TextField.this.cursor = 0;
//    }
//
//    protected void goEnd(boolean jump) {
//        TextField.this.cursor = TextField.this.text.length();
//    }
//
//    public boolean keyDown(InputEvent event, int keycode) {
//        if (TextField.this.disabled) {
//            return false;
//        } else {
//            TextField.this.cursorOn = TextField.this.focused;
//            TextField.this.blinkTask.cancel();
//            if (TextField.this.focused) {
//                Timer.schedule(TextField.this.blinkTask, TextField.this.blinkTime, TextField.this.blinkTime);
//            }
//
//            if (!TextField.this.hasKeyboardFocus()) {
//                return false;
//            } else {
//                boolean repeat = false;
//                boolean ctrl = UIUtils.ctrl();
//                boolean jump = ctrl && !TextField.this.passwordMode;
//                boolean handled = true;
//                if (ctrl) {
//                    switch(keycode) {
//                        case 29:
//                            TextField.this.selectAll();
//                            return true;
//                        case 31:
//                        case 124:
//                            TextField.this.copy();
//                            return true;
//                        case 50:
//                            TextField.this.paste(TextField.this.clipboard.getContents(), true);
//                            repeat = true;
//                            break;
//                        case 52:
//                            TextField.this.cut(true);
//                            return true;
//                        case 54:
//                            String oldText = TextField.this.text;
//                            TextField.this.setText(TextField.this.undoText);
//                            TextField.this.undoText = oldText;
//                            TextField.this.updateDisplayText();
//                            return true;
//                        default:
//                            handled = false;
//                    }
//                }
//
//                if (UIUtils.shift()) {
//                    label75: {
//                        switch(keycode) {
//                            case 112:
//                                TextField.this.cut(true);
//                                break;
//                            case 124:
//                                TextField.this.paste(TextField.this.clipboard.getContents(), true);
//                        }
//
//                        int temp = TextField.this.cursor;
//                        switch(keycode) {
//                            case 3:
//                                this.goHome(jump);
//                                handled = true;
//                                break;
//                            case 21:
//                                TextField.this.moveCursor(false, jump);
//                                repeat = true;
//                                handled = true;
//                                break;
//                            case 22:
//                                TextField.this.moveCursor(true, jump);
//                                repeat = true;
//                                handled = true;
//                                break;
//                            case 123:
//                                this.goEnd(jump);
//                                handled = true;
//                                break;
//                            default:
//                                break label75;
//                        }
//
//                        if (!TextField.this.hasSelection) {
//                            TextField.this.selectionStart = temp;
//                            TextField.this.hasSelection = true;
//                        }
//                    }
//                } else {
//                    switch(keycode) {
//                        case 3:
//                            this.goHome(jump);
//                            TextField.this.clearSelection();
//                            handled = true;
//                            break;
//                        case 21:
//                            TextField.this.moveCursor(false, jump);
//                            TextField.this.clearSelection();
//                            repeat = true;
//                            handled = true;
//                            break;
//                        case 22:
//                            TextField.this.moveCursor(true, jump);
//                            TextField.this.clearSelection();
//                            repeat = true;
//                            handled = true;
//                            break;
//                        case 123:
//                            this.goEnd(jump);
//                            TextField.this.clearSelection();
//                            handled = true;
//                    }
//                }
//
//                TextField.this.cursor = MathUtils.clamp(TextField.this.cursor, 0, TextField.this.text.length());
//                if (repeat) {
//                    this.scheduleKeyRepeatTask(keycode);
//                }
//
//                return handled;
//            }
//        }
//    }
//
//    protected void scheduleKeyRepeatTask(int keycode) {
//        if (!TextField.this.keyRepeatTask.isScheduled() || TextField.this.keyRepeatTask.keycode != keycode) {
//            TextField.this.keyRepeatTask.keycode = keycode;
//            TextField.this.keyRepeatTask.cancel();
//            Timer.schedule(TextField.this.keyRepeatTask, TextField.keyRepeatInitialTime, TextField.keyRepeatTime);
//        }
//
//    }
//
//    public boolean keyUp(InputEvent event, int keycode) {
//        if (TextField.this.disabled) {
//            return false;
//        } else {
//            TextField.this.keyRepeatTask.cancel();
//            return true;
//        }
//    }
//
//    protected boolean checkFocusTraversal(char character) {
//        return TextField.this.focusTraversal && (character == '\t' || (character == '\r' || character == '\n') && (UIUtils.isAndroid || UIUtils.isIos));
//    }
//
//    public boolean keyTyped(InputEvent event, char character) {
//        if (TextField.this.disabled) {
//            return false;
//        } else {
//            switch(character) {
//                case '\b':
//                case '\t':
//                case '\n':
//                case '\r':
//                    break;
//                case '\u000b':
//                case '\f':
//                default:
//                    if (character < ' ') {
//                        return false;
//                    }
//            }
//
//            if (!TextField.this.hasKeyboardFocus()) {
//                return false;
//            } else if (UIUtils.isMac && Gdx.input.isKeyPressed(63)) {
//                return true;
//            } else {
//                if (this.checkFocusTraversal(character)) {
//                    TextField.this.next(UIUtils.shift());
//                } else {
//                    boolean enter = character == '\r' || character == '\n';
//                    boolean delete = character == 127;
//                    boolean backspace = character == '\b';
//                    boolean add = enter ? TextField.this.writeEnters : !TextField.this.onlyFontChars || TextField.this.style.font.getData().hasGlyph(character);
//                    boolean remove = backspace || delete;
//                    if (add || remove) {
//                        String oldText = TextField.this.text;
//                        int oldCursor = TextField.this.cursor;
//                        if (remove) {
//                            if (TextField.this.hasSelection) {
//                                TextField.this.cursor = TextField.this.delete(false);
//                            } else {
//                                if (backspace && TextField.this.cursor > 0) {
//                                    TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor - 1) + TextField.this.text.substring(TextField.this.cursor--);
//                                    TextField.this.renderOffset = 0.0F;
//                                }
//
//                                if (delete && TextField.this.cursor < TextField.this.text.length()) {
//                                    TextField.this.text = TextField.this.text.substring(0, TextField.this.cursor) + TextField.this.text.substring(TextField.this.cursor + 1);
//                                }
//                            }
//                        }
//
//                        String insertion;
//                        if (add && !remove) {
//                            if (!enter && TextField.this.filter != null && !TextField.this.filter.acceptChar(TextField.this, character)) {
//                                return true;
//                            }
//
//                            if (!TextField.this.withinMaxLength(TextField.this.text.length() - (TextField.this.hasSelection ? Math.abs(TextField.this.cursor - TextField.this.selectionStart) : 0))) {
//                                return true;
//                            }
//
//                            if (TextField.this.hasSelection) {
//                                TextField.this.cursor = TextField.this.delete(false);
//                            }
//
//                            insertion = enter ? "\n" : String.valueOf(character);
//                            TextField.this.text = TextField.this.insert(TextField.this.cursor++, insertion, TextField.this.text);
//                        }
//
//                        insertion = TextField.this.undoText;
//                        if (TextField.this.changeText(oldText, TextField.this.text)) {
//                            long time = System.currentTimeMillis();
//                            if (time - 750L > TextField.this.lastChangeTime) {
//                                TextField.this.undoText = oldText;
//                            }
//
//                            TextField.this.lastChangeTime = time;
//                            TextField.this.updateDisplayText();
//                        } else {
//                            TextField.this.cursor = oldCursor;
//                        }
//                    }
//                }
//
//                if (TextField.this.listener != null) {
//                    TextField.this.listener.keyTyped(TextField.this, character);
//                }
//
//                return true;
//            }
//        }
//    }
//}
