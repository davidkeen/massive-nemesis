"""
Calculate the running time and storage requirements for a time lapse.
Inputs are: recording time, recording frame rate, final frame rate and image size.
"""

import argparse

parser = argparse.ArgumentParser(description='Time lapse calculator')
parser.add_argument('recording_time', type=int, help='Real recording time (minutes)')
parser.add_argument('recording_frame_rate', type=int, help='Recording frame rate (seconds/frame)')
parser.add_argument('final_frame_rate', type=int, help='Final frame rate (frames/second)')
parser.add_argument('image_size', type=int, help='Size of image files (MB)')
args = parser.parse_args()

recording_seconds = args.recording_time * 60
recording_fps = 1 / args.recording_frame_rate
frame_count = recording_seconds * recording_fps
lapse_time = frame_count / args.final_frame_rate / 60
total_size = frame_count * args.image_size

print()
print('{0:12} {1:12} {2:12} {3:12} {4:12}'.format('In FPS', 'Out FPS', 'Mins', 'Images', 'MB'))
print('-' * 61)
print('{0:<12.2f} {1:<12} {2:<12.2f} {3:<12.2f} {4:<12.2f}'.format(recording_fps, args.final_frame_rate, lapse_time,
                                                                   frame_count, total_size))
print()
