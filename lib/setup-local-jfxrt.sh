HERE=$(cd "$(dirname "$BASH_SOURCE")" && pwd)
if [ -z "$HERE" ] || [ ! -f "$HERE"/jfxrt.jar ]; then
    HERE=$(cd "$(dirname "$0")" && pwd)
fi
if [ -z "$HERE" ] || [ ! -f "$HERE"/jfxrt.jar ]; then
    HERE=$PWD
fi
if [ -z "$HERE" ] || [ ! -f "$HERE"/jfxrt.jar ]; then
    HERE=$PWD/lib
fi
if [ -z "$HERE" ] || [ ! -f "$HERE"/jfxrt.jar ]; then
    echo "Failed to autodetect directory."
    exit 1
fi

CLASSPATH="$HERE"/jfxrt.jar:"$HERE"/jfxswt.jar:.
export CLASSPATH
