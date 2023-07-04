SCRIPT_START
{
    LVAR_INT player, actKey1, actKey2, menu, selectedItem, isLooped, lockX, lockF
    LVAR_TEXT_LABEL16 animName, ifpFile

    GET_PLAYER_CHAR 0, player
    GOSUB ReadKeybinds
    GOTO Main

  Main:
    SET_PLAYER_CONTROL 0, TRUE
    SET_PLAYER_ENTER_CAR_BUTTON 0, TRUE
    
    WHILE TRUE
        WAIT 0
        IF GOSUB CheckActivation
            WHILE GOSUB CheckActivation
                WAIT 0
            ENDWHILE

            SET_PLAYER_CONTROL 0, FALSE
            SET_PLAYER_ENTER_CAR_BUTTON 0, FALSE

            GOSUB ResetVariables

            CREATE_MENU AMMTITL (200.0, 140.0), 220.0, 1, TRUE, TRUE, 0, menu
            SET_MENU_COLUMN menu, 0, AMMCHOS, (AMMI01, AMMI02, AMMI03, AMMI04, AMMI05, AMMI06, AMMI07, DUMMY, DUMMY, DUMMY, DUMMY, DUMMY)
            GOSUB GetSelectedItem

            SWITCH selectedItem
                CASE 0
                    GOTO CreateMenuGeneral1
                    BREAK
                CASE 1
                    GOTO CreateMenuSitting
                    BREAK
                CASE 2
                    GOTO CreateMenuDancing
                    BREAK
                CASE 3
                    GOTO CreateMenuChatting
                    BREAK
                CASE 4
                    GOTO CreateMenuGang
                    BREAK
                CASE 5
                    GOTO CreateMenuIdleStanding
                    BREAK
                CASE 6
                    GOTO CreateMenuWeapon
                    BREAK
            ENDSWITCH
        ENDIF
    ENDWHILE

  ReadKeybinds:
    IF NOT READ_INT_FROM_INI_FILE ("CLEO\AnimMenu.ini", "Controls", "ActivationKey1"), actKey1
    OR NOT READ_INT_FROM_INI_FILE ("CLEO\AnimMenu.ini", "Controls", "ActivationKey2"), actKey2
        // Revert to the default keybinds
        actKey1 = VK_KEY_N
        actKey2 = VK_KEY_M

        PRINT_STRING_NOW "~g~Anim Menu~s~: Could not read the keybinds, using the default ones (N + M)", 2500
    ENDIF
    RETURN

  CheckActivation:
    IF IS_KEY_PRESSED actKey1
    AND IS_KEY_PRESSED actKey2
    AND IS_PLAYER_PLAYING 0
    AND NOT IS_CHAR_IN_WATER player
    AND NOT IS_CHAR_IN_ANY_CAR player
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
    RETURN

  GetSelectedItem:
    WHILE TRUE
        WAIT 0
        // Use the 'Space' key to determine the selected item
        IF IS_KEY_PRESSED VK_SPACE
            GET_MENU_ITEM_SELECTED menu, selectedItem
            WHILE IS_KEY_PRESSED VK_SPACE
                WAIT 0
            ENDWHILE

            BREAK
        ENDIF

        IF GOSUB InterruptionOccured
            // Delete the menu and jump to the beginning of the script
            DELETE_MENU menu
            GOTO Main
        ENDIF
    ENDWHILE
    RETURN

  ResetVariables:
    ifpFile  = "PED"
    animName = ""
    isLooped = FALSE
    lockX    = FALSE
    lockF    = FALSE
    RETURN

  CreateMenuGeneral1:
    SET_MENU_COLUMN menu, 0, AMGEP01, (AMGEI01, AMGEI02, AMGEI03, AMGEI04, AMGEI05, AMGEI06, AMGEI07, AMGEI08, AMGEI09, AMGEI10, AMGEI11, AMMNEXT)
    // Get the selected animation
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Wave 1
            ifpFile  = "ON_LOOKERS"
            animName = "WAVE_LOOP"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Wave 2
            animName = "ENDCHAT_03"
            BREAK

        CASE 2  // Lean (back)
            ifpFile  = "GANGS"
            animName = "LEANIDLE"
            isLooped = TRUE
            BREAK

        CASE 3  // Lean (left)
            ifpFile  = "MISC"
            animName = "PLYRLEAN_LOOP"
            isLooped = TRUE
            BREAK

        CASE 4  // Salute/point up
            ifpFile  = "ON_LOOKERS"
            animName = "POINTUP_LOOP"
            isLooped = TRUE
            BREAK

        CASE 5  // Eat
            ifpFile  = "FOOD"
            animName = "EAT_BURGER"
            isLooped = TRUE
            BREAK

        CASE 6  // Drink
            ifpFile  = "BAR"
            animName = "DNK_STNDM_LOOP"
            isLooped = TRUE
            BREAK

        CASE 7  // Smoke
            ifpFile  = "GANGS"
            animName = "SMKCIG_PRTL"
            isLooped = TRUE
            BREAK

        CASE 8  // Smoke (leaning)
            ifpFile  = "SMOKING"
            animName = "M_SMKLEAN_LOOP"
            isLooped = TRUE
            BREAK

        CASE 9  // Laugh
            ifpFile  = "RAPPING"
            animName = "LAUGH_01"
            isLooped = TRUE
            BREAK

        CASE 10  // Cry
            ifpFile  = "GRAVEYARD"
            animName = "MRNF_LOOP"
            isLooped = TRUE
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral2
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral2:
    SET_MENU_COLUMN menu, 0, AMGEP02, (AMGEI12, AMGEI13, AMGEI14, AMGEI15, AMGEI16, AMGEI17, AMGEI18, AMGEI19, AMGEI20, AMGEI21, AMGEI22, AMMNEXT)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Hands up
            animName = "HANDSUP"
            lockF    = TRUE
            BREAK
        
        CASE 1  // Cross arms
            ifpFile  = "COP_AMBIENT"
            animName = "COPLOOK_LOOP"
            isLooped = TRUE
            BREAK

        CASE 2  // Think
            ifpFile  = "COP_AMBIENT"
            animName = "COPLOOK_THINK"
            isLooped = TRUE
            BREAK

        CASE 3  // Look at watch
            ifpFile  = "COP_AMBIENT"
            animName = "COPLOOK_WATCH"
            isLooped = TRUE
            BREAK

        CASE 4  // Flip off
            animName = "FUCKU"
            isLooped = TRUE
            BREAK

        CASE 5  // Signal a taxi
            ifpFile  = "MISC"
            animName = "HIKER_POSE"
            lockF    = TRUE
            BREAK

        CASE 6  // Grab/take
            ifpFile  = "MISC"
            animName = "GRAB_R"
            BREAK

        CASE 7  // Drunk walk
            animName = "WALK_DRUNK"
            isLooped = TRUE
            BREAK

        CASE 8  // Rob/aim
            ifpFile  = "SHOP"
            animName = "ROB_Loop_Threat"
            isLooped = TRUE
            BREAK

        CASE 9  // Cower/hide
            animName = "COWER"
            isLooped = TRUE
            BREAK

        CASE 10  // Shout
            ifpFile  = "ON_LOOKERS"
            animName = "SHOUT_02"
            isLooped = TRUE
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral3
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral3:
    SET_MENU_COLUMN menu, 0, AMGEP03, (AMGEI23, AMGEI24, AMGEI25, AMGEI26, AMGEI27, AMGEI28, AMGEI29, AMGEI30, AMGEI31, AMGEI32, AMGEI33, AMMNEXT)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Puke
            ifpFile  = "FOOD"
            animName = "EAT_VOMIT_P"
            BREAK
        
        CASE 1  // Wank
            ifpFile  = "PAULNMAC"
            animName = "WANK_LOOP"
            isLooped = TRUE
            BREAK

        CASE 2  // Piss
            ifpFile  = "PAULNMAC"
            animName = "PISS_LOOP"
            isLooped = TRUE
            BREAK

        CASE 3  // Kiss
            ifpFile  = "KISSING"
            animName = "GRLFRD_KISS_02"
            BREAK

        CASE 4  // Use ATM
            animName = "ATM"
            isLooped = TRUE
            BREAK

        CASE 5  // Tired (catch breath)
            ifpFile  = "FAT"
            animName = "IDLE_TIRED"
            isLooped = TRUE
            BREAK

        CASE 6  // Point forward
            ifpFile  = "ON_LOOKERS"
            animName = "POINT_LOOP"
            isLooped = TRUE
            BREAK

        CASE 7  // Boxing
            ifpFile  = "GYMNASIUM"
            animName = "GYMSHADOWBOX"
            isLooped = TRUE
            BREAK

        CASE 8  // Protest/riot 1
            ifpFile  = "RIOT"
            animName = "RIOT_CHANT"
            isLooped = TRUE
            BREAK

        CASE 9  // Protest/riot 2
            ifpFile  = "RIOT"
            animName = "RIOT_PUNCHES"
            isLooped = TRUE
            BREAK

        CASE 10  // Perform CPR
            ifpFile  = "MEDIC"
            animName = "CPR"
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral4
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral4:
    SET_MENU_COLUMN menu, 0, AMGEP04, (AMGEI34, AMGEI35, AMGEI36, AMGEI37, AMGEI38, AMGEI39, AMGEI40, AMGEI41, AMGEI42, AMGEI43, AMGEI44, AMMNEXT)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Die (on stomach)
            animName = "KO_SHOT_STOM"
            lockF    = TRUE
            BREAK
        
        CASE 1  // Injured (on wall)
            ifpFile  = "SWAT"
            animName = "GNSTWALL_INJURD"
            isLooped = TRUE
            BREAK

        CASE 2  // Sleep
            ifpFile  = "CRACK"
            animName = "CRCKIDLE2"
            isLooped = TRUE
            BREAK

        CASE 3  // Lie on ground (crack)
            ifpFile  = "CRACK"
            animName = "CRCKIDLE4"
            isLooped = TRUE
            BREAK

        CASE 4  // Win
            ifpFile  = "OTB"
            animName = "WTCHRACE_WIN"
            isLooped = TRUE
            BREAK

        CASE 5  // Lose
            ifpFile  = "OTB"
            animName = "WTCHRACE_LOSE"
            BREAK

        CASE 6  // Swipe card
            ifpFile  = "HEIST9"
            animName = "USE_SWIPECARD"
            BREAK

        CASE 7  // Use switch/button
            ifpFile  = "CRIB"
            animName = "CRIB_USE_SWITCH"
            BREAK

        CASE 8  // Wash hands
            ifpFile  = "INT_HOUSE"
            animName = "WASH_UP"
            isLooped = TRUE
            BREAK

        CASE 9  // Plant bomb
            ifpFile  = "BOMBER"
            animName = "BOM_PLANT_LOOP"
            isLooped = TRUE
            BREAK

        CASE 10  // Beckon
            ifpFile  = "RYDER"
            animName = "RYD_BECKON_01"
            isLooped = TRUE
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral5
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral5:
    SET_MENU_COLUMN menu, 0, AMGEP05, (AMGEI45, AMGEI46, AMGEI47, AMGEI48, AMGEI49, AMGEI50, AMGEI51, AMGEI52, AMGEI53, AMGEI54, AMGEI55, AMMNEXT)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Scratch balls
            ifpFile  = "MISC"
            animName = "SCRATCHBALLS_01"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Bitch slap
            ifpFile  = "MISC"
            animName = "BITCHSLAP"
            isLooped = TRUE
            BREAK

        CASE 2  // Taichi
            ifpFile  = "PARK"
            animName = "TAI_CHI_LOOP"
            isLooped = TRUE
            BREAK

        CASE 3  // Kung fu
            ifpFile  = "FIGHT_C"
            animName = "FIGHTC_SPAR"
            isLooped = TRUE
            BREAK

        CASE 4  // Pick up
            ifpFile  = "MISC"
            animName = "PICKUP_BOX"
            BREAK

        CASE 5  // Facepalm
            ifpFile  = "MISC"
            animName = "PLYR_SHKHEAD"
            isLooped = TRUE
            BREAK

        CASE 6  // Talk on phone
            animName = "PHONE_TALK"
            isLooped = TRUE
            BREAK

        CASE 7  // Sneak walk
            animName = "PLAYER_SNEAK"
            isLooped = TRUE
            BREAK

        CASE 8  // Spray
            ifpFile  = "GRAFFITI"
            animName = "SPRAYCAN_FIRE"
            isLooped = TRUE
            BREAK

        CASE 9  // Rob safe
            ifpFile  = "ROB_BANK"
            animName = "CAT_SAFE_ROB"
            isLooped = TRUE
            BREAK

        CASE 10  // Arrest
            animName = "ARRESTGUN"
            lockF    = TRUE
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral6
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral6:
    SET_MENU_COLUMN menu, 0, AMGEP06, (AMGEI56, AMGEI57, AMGEI58, AMGEI59, AMGEI60, AMGEI61, AMGEI62, AMGEI63, AMGEI64, AMGEI65, AMGEI66, AMMNEXT)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Pay cash
            ifpFile  = "DEALER"
            animName = "SHOP_PAY"
            BREAK
        
        CASE 1  // Drug Dealer (idle)
            ifpFile  = "DEALER"
            animName = "DEALER_IDLE"
            isLooped = TRUE
            BREAK

        CASE 2  // Drug Dealer (sell)
            ifpFile  = "DEALER"
            animName = "DEALER_DEAL"
            BREAK

        CASE 3  // Rap 1
            ifpFile  = "RAPPING"
            animName = "RAP_B_LOOP"
            isLooped = TRUE
            BREAK

        CASE 4  // Rap 2
            ifpFile  = "RAPPING"
            animName = "RAP_C_LOOP"
            isLooped = TRUE
            BREAK

        CASE 5  // Gym workout
            ifpFile  = "BENCHPRESS"
            animName = "GYM_BP_CELEBRATE"
            BREAK

        CASE 6  // Kick
            ifpFile  = "POLICE"
            animName = "DOOR_KICK"
            isLooped = TRUE
            BREAK

        CASE 7  // Push
            ifpFile  = "GANGS"
            animName = "SHAKE_CARA"
            isLooped = TRUE
            BREAK

        CASE 8  // Headbutt
            ifpFile  = "WAYFARER"
            animName = "WF_FWD"
            isLooped = TRUE
            BREAK

        CASE 9  // Open sliding door
            ifpFile  = "AIRPORT"
            animName = "THRW_BARL_THRW"
            BREAK

        CASE 10  // Fix car
            ifpFile  = "CAR"
            animName = "FIXN_CAR_LOOP"
            isLooped = TRUE
            BREAK

        CASE 11  // Next page
            GOTO CreateMenuGeneral7
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGeneral7:
    SET_MENU_COLUMN menu, 0, AMGEP07, (AMGEI67, AMGEI68, AMGEI69, AMGEI70, AMGEI71, AMGEI72, AMGEI73, AMGEI74, AMGEI75, AMGEI76, AMGEI77, AMGEI78)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Basket (idle)
            ifpFile  = "BSKTBALL"
            animName = "BBALL_IDLELOOP"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Basket (defend)
            ifpFile  = "BSKTBALL"
            animName = "BBALL_DEF_LOOP"
            isLooped = TRUE
            BREAK

        CASE 2  // Basket (dunk)
            ifpFile  = "BSKTBALL"
            animName = "BBALL_DNK"
            BREAK

        CASE 3  // Throw
            ifpFile  = "GRENADE"
            animName = "WEAPON_THROWU"
            BREAK

        CASE 4  // Throw (far)
            ifpFile  = "GRENADE"
            animName = "WEAPON_THROW"
            BREAK

        CASE 5  // Put down
            ifpFile  = "CARRY"
            animName = "PUTDWN"
            BREAK

        CASE 6  // Slap ass
            ifpFile  = "SWEET"
            animName = "SWEET_ASS_SLAP"
            isLooped = TRUE
            BREAK

        CASE 7  // Dying
            ifpFile  = "WUZI"
            animName = "CS_DEAD_GUY"
            isLooped = TRUE
            BREAK

        CASE 8  // Use vending machine
            ifpFile  = "VENDING"
            animName = "VEND_USE"
            BREAK

        CASE 9  // Signal to stop
            ifpFile  = "POLICE"
            animName = "COPTRAF_STOP"
            isLooped = TRUE
            BREAK

        CASE 10  // Search car trunk
            ifpFile  = "POLICE"
            animName = "PLC_DRGBST_02"
            isLooped = TRUE
            BREAK

        CASE 11  // Take cover
            ifpFile  = "HEIST9"
            animName = "SWT_WLLPK_R"
            isLooped = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuSitting:
    SET_MENU_COLUMN menu, 0, AMMI02, (AMSII01, AMSII02, AMSII03, AMSII04, AMSII05, AMSII06, AMSII07, AMSII08, AMSII09, AMSII10, AMSII11, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Lay on ground 1
            ifpFile  = "BEACH"
            animName = "LAY_BAC_LOOP"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Lay on ground 2
            ifpFile  = "BEACH"
            animName = "PARKSIT_M_LOOP"
            isLooped = TRUE
            BREAK

        CASE 2  // Lay on ground 3
            ifpFile  = "BEACH"
            animName = "PARKSIT_W_LOOP"
            isLooped = TRUE
            BREAK

        CASE 3  // Lay on ground 4
            ifpFile  = "BEACH"
            animName = "SITNWAIT_LOOP_W"
            isLooped = TRUE
            BREAK

        CASE 4  // Sit and eat
            ifpFile  = "FOOD"
            animName = "FF_SIT_EAT1"
            isLooped = TRUE
            BREAK

        CASE 5  // Sit and die
            ifpFile  = "FOOD"
            animName = "FF_DIE_FWD"
            lockF    = TRUE
            BREAK

        CASE 6  // Sit on chair
            animName = "SEAT_IDLE"
            lockF    = TRUE
            BREAK

        CASE 7  // Sit on chair (relaxed 1)
            ifpFile  = "ATTRACTORS"
            animName = "STEPSIT_LOOP"
            isLooped = TRUE
            BREAK

        CASE 8  // Sit on chair (relaxed 2)
            ifpFile  = "INT_HOUSE"
            animName = "LOU_LOOP"
            isLooped = TRUE
            BREAK

        CASE 9  // Sit and talk
            ifpFile  = "MISC"
            animName = "SEAT_TALK_02"
            isLooped = TRUE
            BREAK

        CASE 10  // Sit and look at watch
            ifpFile  = "MISC"
            animName = "SEAT_WATCH"
            lockF    = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuDancing:
    SET_MENU_COLUMN menu, 0, AMMI03, (AMDAI01, AMDAI02, AMDAI03, AMDAI04, AMDAI05, AMDAI06, AMDAI07, AMDAI08, AMDAI09, AMDAI10, AMDAI11, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Generic dance
            ifpFile  = "DANCING"
            animName = "DANCE_LOOP"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Dance 1
            ifpFile  = "DANCING"
            animName = "DAN_DOWN_A"
            isLooped = TRUE
            BREAK

        CASE 2  // Dance 2
            ifpFile  = "DANCING"
            animName = "DAN_RIGHT_A"
            isLooped = TRUE
            BREAK

        CASE 3  // Dance 3
            ifpFile  = "DANCING"
            animName = "DNCE_M_E"
            isLooped = TRUE
            BREAK

        CASE 4  // Dance 4
            ifpFile  = "STRIP"
            animName = "STRIP_D"
            isLooped = TRUE
            BREAK

        CASE 5  // Dance 5
            ifpFile  = "STRIP"
            animName = "STRIP_E"
            isLooped = TRUE
            BREAK

        CASE 6  // Dance 6
            ifpFile  = "STRIP"
            animName = "STRIP_F"
            isLooped = TRUE
            BREAK

        CASE 7  // Dance 7
            ifpFile  = "STRIP"
            animName = "STRIP_G"
            isLooped = TRUE
            BREAK

        CASE 8  // Dance 8
            ifpFile  = "STRIP"
            animName = "STR_LOOP_A"
            isLooped = TRUE
            BREAK

        CASE 9  // Dance 9
            ifpFile  = "STRIP"
            animName = "STR_LOOP_B"
            isLooped = TRUE
            BREAK

        CASE 10  // Dance 10
            ifpFile  = "STRIP"
            animName = "STR_LOOP_C"
            isLooped = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuChatting:
    SET_MENU_COLUMN menu, 0, AMMI04, (AMCHI01, AMCHI02, AMCHI03, AMCHI04, AMCHI05, AMCHI06, AMCHI07, AMCHI08, AMCHI09, AMCHI10, AMCHI11, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Chat 1
            animName = "IDLE_CHAT"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Chat 2
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKB"
            isLooped = TRUE
            BREAK

        CASE 2  // Chat 3
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKC"
            isLooped = TRUE
            BREAK

        CASE 3  // Chat 4
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKF"
            isLooped = TRUE
            BREAK

        CASE 4  // Chat 5
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKH"
            isLooped = TRUE
            BREAK

        CASE 5  // Chat 6
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKG"
            isLooped = TRUE
            BREAK

        CASE 6  // Chat 7
            ifpFile  = "GANGS"
            animName = "PRTIAL_GNGTLKE"
            isLooped = TRUE
            BREAK

        CASE 7  // Chat (window)
            ifpFile  = "MISC"
            animName = "BNG_WNDW_02"
            isLooped = TRUE
            BREAK

        CASE 8  // Argue 1
            ifpFile  = "LOWRIDER"
            animName = "PRTIAL_GNGTLKE"
            isLooped = TRUE
            BREAK

        CASE 9  // Argue 2
            ifpFile  = "LOWRIDER"
            animName = "PRTIAL_GNGTLKG"
            isLooped = TRUE
            BREAK

        CASE 10  // Argue 3
            ifpFile  = "LOWRIDER"
            animName = "PRTIAL_GNGTLKH"
            isLooped = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuGang:
    SET_MENU_COLUMN menu, 0, AMMI05, (AMGAI01, AMGAI02, AMGAI03, AMGAI04, AMGAI05, AMGAI06, AMGAI07, AMGAI08, AMGAI09, AMGAI10, DUMMY, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Yes
            ifpFile  = "GANGS"
            animName = "INVITE_YES"
            BREAK
        
        CASE 1  // No
            ifpFile  = "GANGS"
            animName = "INVITE_NO"
            BREAK

        CASE 2  // Handshake 1
            ifpFile  = "GANGS"
            animName = "HNDSHKFA"
            BREAK

        CASE 3  // Handshake 2 (fast)
            ifpFile  = "GANGS"
            animName = "HNDSHKBA"
            BREAK

        CASE 4  // Handshake 3 (slow)
            ifpFile  = "GANGS"
            animName = "PRTIAL_HNDSHK_BIZ_01"
            BREAK

        CASE 5  // Handshake 4
            ifpFile  = "GANGS"
            animName = "PRTIAL_HNDSHK_01"
            BREAK

        CASE 6  // Gang sign 1
            ifpFile  = "GHANDS"
            animName = "GSIGN1LH"
            BREAK

        CASE 7  // Gang sign 2
            ifpFile  = "GHANDS"
            animName = "GSIGN2LH"
            BREAK

        CASE 8  // Gang sign 3
            ifpFile  = "GHANDS"
            animName = "GSIGN5LH"
            BREAK

        CASE 9  // Gang sign 4
            ifpFile  = "GHANDS"
            animName = "GSIGN4LH"
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuIdleStanding:
    SET_MENU_COLUMN menu, 0, AMMI06, (AMIDI01, AMIDI02, AMIDI03, AMIDI04, AMIDI05, AMIDI06, AMIDI07, AMIDI08, DUMMY, DUMMY, DUMMY, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Idle 1
            ifpFile  = "PLAYIDLES"
            animName = "SHIFT"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Idle 2
            ifpFile  = "PLAYIDLES"
            animName = "STRETCH"
            isLooped = TRUE
            BREAK

        CASE 2  // Idle 3
            ifpFile  = "PLAYIDLES"
            animName = "TIME"
            isLooped = TRUE
            BREAK

        CASE 3  // Idle 4
            animName = "XPRESSSCRATCH"
            isLooped = TRUE
            BREAK

        CASE 4  // Idle 5
            ifpFile  = "COP_AMBIENT"
            animName = "COPLOOK_SHAKE"
            isLooped = TRUE
            BREAK

        CASE 5  // Idle 6
            ifpFile  = "DEALER"
            animName = "DEALER_IDLE_01"
            isLooped = TRUE
            BREAK

        CASE 6  // Idle (bartender)
            ifpFile  = "BAR"
            animName = "BARSERVE_LOOP"
            isLooped = TRUE
            BREAK

        CASE 7  // Idle (upside down)
            ifpFile  = "DAM_JUMP"
            animName = "DAM_DIVE_LOOP"
            isLooped = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  CreateMenuWeapon:
    SET_MENU_COLUMN menu, 0, AMMI07, (AMWEI01, AMWEI02, AMWEI03, AMWEI04, AMWEI05, AMWEI06, AMWEI07, DUMMY, DUMMY, DUMMY, DUMMY, DUMMY)
    GOSUB GetSelectedItem

    SWITCH selectedItem
        CASE 0  // Hold bat
            ifpFile  = "CRACK"
            animName = "BBALBAT_IDLE_01"
            isLooped = TRUE
            BREAK
        
        CASE 1  // Reload (Rifle)
            ifpFile  = "RIFLE"
            animName = "RIFLE_LOAD"
            BREAK

        CASE 2  // Reload (SMG)
            ifpFile  = "UZI"
            animName = "UZI_RELOAD"
            BREAK

        CASE 3  // Cock/reload Shotgun
            ifpFile  = "BUDDY"
            animName = "BUDDY_RELOAD"
            BREAK

        CASE 4  // Cock/reload Pistol
            ifpFile  = "COLT45"
            animName = "COLT45_RELOAD"
            BREAK

        CASE 5  // Cock/reload Pistol (idle)
            ifpFile  = "COLT45"
            animName = "COLT45_RELOAD"
            lockF    = TRUE
            BREAK

        CASE 6  // Dildo (idle)
            ifpFile  = "DILDO"
            animName = "DILDO_IDLE"
            isLooped = TRUE
            BREAK
    ENDSWITCH
    GOTO PlayAnim

  PlayAnim:
    DELETE_MENU menu
    SET_PLAYER_CONTROL 0, TRUE

    // Check if the ifp file is not 'PED' (since it's already loaded by the game, thus there's no need to load it again)
    IF NOT ifpFile = "PED"
        REQUEST_ANIMATION $ifpFile
        WHILE NOT HAS_ANIMATION_LOADED $ifpFile
            WAIT 0
        ENDWHILE
    ELSE
        // Set 'lockX' to true if the ifp file is 'PED'
        lockX = TRUE
    ENDIF

    CLEAR_CHAR_TASKS_IMMEDIATELY player
    
    TASK_PLAY_ANIM_NON_INTERRUPTABLE player, ($animName, $ifpFile), 4.1f, (isLooped, lockX, TRUE, lockF), 0
    PRINT_STRING_NOW "Press ~y~F~s~, ~y~Enter~s~ or ~y~Space~s~ to cancel the animation.", 3000

    WHILE TRUE
        WAIT 0
        IF GOSUB InterruptionOccured
            BREAK
        ENDIF
    ENDWHILE
    
    CLEAR_CHAR_TASKS_IMMEDIATELY player
    IF NOT ifpFile = "PED"  // Used to avoid releasing the 'PED' ifp file as it might crash the game
        REMOVE_ANIMATION $ifpFile
    ENDIF

    WAIT 100
    GOTO Main

  InterruptionOccured:
    IF IS_KEY_PRESSED VK_RETURN
    OR IS_KEY_PRESSED VK_KEY_F
    OR IS_KEY_PRESSED VK_SPACE
    OR NOT IS_PLAYER_PLAYING 0
    OR IS_CHAR_IN_ANY_CAR player
    OR IS_CHAR_IN_WATER player
    OR IS_PLAYER_USING_JETPACK 0
        RETURN_TRUE
    ELSE
        RETURN_FALSE
    ENDIF
    RETURN
}
SCRIPT_END