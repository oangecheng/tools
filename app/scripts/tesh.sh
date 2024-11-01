#!/bin/bash


INTENT_FILTER="debug.kwai.live.adb"
ACTION="action"
DATA="data"


send_broadcast() {
    local action="$1"
    local data="$2"
    adb shell am broadcast -a "$INTENT_FILTER" --es "$ACTION" "$action" --es "$DATA" "$data"
}


operation_clear_sp() {
    # 广播的动作名称
    local act="CLEAR_SP"
    local SP_KEY="$1"
    send_broadcast "$act" "$SP_KEY"
    echo "执行清理sp命令."
}


# Function to perform operation B with parameter
operation_toast() {
    local data="$1"
    local act="TOAST"
    # Place the command(s) for operation B here
    send_broadcast "$act" "$data"
}

# Function to display script usage
showUsage() {
    echo "Usage: $0 <operation> [<param>]"
    echo "Operations:"
    echo "  a        Perform operation A"
    echo "  b        Perform operation B, requires param"
}



# Check if at least one argument is provided
if [ $# -eq 0 ]; then
    showUsage
    exit 1
fi

# Handle operations based on first argument
operation="$1"
case "$operation" in
    toast)
      if [ $# -eq 2 ]; then
        operation_toast "$2"
      else
        showUsage
        exit 1
      fi
      ;;

    clearsp)
        if [ $# -eq 2 ]; then
            operation_clear_sp "$2"
        else
            showUsage
            exit 1
        fi
        ;;
    *)
        echo "Error: Unknown operation '$operation'"
        showUsage
        exit 1
        ;;
esac
